package com.scraper.gpt;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.scraper.gpt.yandexgpt.YandexGptRequest;
import com.scraper.gpt.yandexgpt.YandexGptResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ParametersAreNonnullByDefault
public class GptClient {

    private static final ObjectMapper OBJECT_MAPPER = create();
    private final HttpClient client;

    private static <T> T parseJson(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            log.error("Cannot parse json " + json, e);
            throw new RuntimeException(e);
        }
    }

    public static <T> String toJson(T object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static ObjectMapper create() {
        return new ObjectMapper()
                .configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE, true)
                .configure(DeserializationFeature.FAIL_ON_MISSING_EXTERNAL_TYPE_ID_PROPERTY, false)
                .registerModule(new JavaTimeModule())
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public String getToken() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .header("Metadata-Flavor", "Google")
                    .uri(URI.create("http://169.254.169.254/computeMetadata/v1/instance/service-accounts/default/token"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return parseJson(response.body(), Token.class)
                    .getAccessToken();
        } catch (Throwable ex) {
            log.error(ex.getMessage(), ex);
            return null;
        }
    }

    @Nullable
    public YandexGptResponse query(YandexGptRequest request) {
        try {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .header("Authorization", "Bearer " + getToken())
                    .uri(URI.create("https://llm.api.cloud.yandex.net/foundationModels/v1/completion"))
                    .POST(HttpRequest.BodyPublishers.ofString(toJson(request)))
                    .build();
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return parseJson(response.body(), YandexGptResponse.class);
        } catch (Throwable ex) {
            log.error(ex.getMessage(), ex);
            return null;
        }
    }
}
