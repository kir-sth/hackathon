package scraper.telegram;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.util.concurrent.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import scraper.config.ScraperConfig;
import scraper.models.TgChannel;
import scraper.models.TgPost;
import scraper.telegram.parser.TelegramHtmlParser;

@Slf4j
@Component
@RequiredArgsConstructor
@SuppressWarnings("UnstableApiUsage")
public class TelegramWebClientImpl implements TelegramWebClient, InitializingBean {

    private static final Comparator<TgPost> COMPARATOR = Comparator.comparing(TgPost::getId, Comparator.reverseOrder());

    private final TelegramHtmlParser parser;
    private final HttpClient client;
    private final ConfusingHeadersProvider confusingHeadersProvider;
    private final ScraperConfig scraperConfig;
    private RateLimiter limiter;



    @Override
    @Nullable
    public TgChannel searchChannel(@Nonnull String channel) {
        return request("https://t.me/" + channel)
                .map(parser::parseChannel)
                .orElse(null);
    }

    @Nonnull
    @Override
    public List<TgPost> getLastPosts(@Nonnull String channel, int count) {
        return getMessages(channel, requestMessages(channel), count);
    }

    @Nonnull
    private List<TgPost> getMessages(@Nonnull String channel, List<TgPost> initMessages, int count) {
        Set<TgPost> result = new TreeSet<>(COMPARATOR);
        List<TgPost> posts = initMessages;
        while (!posts.isEmpty() && result.size() < count) {
            result.addAll(posts);
            posts = requestMessages(channel, posts.get(0).getId());
        }
        return new ArrayList<>(result);
    }

    private List<TgPost> requestMessages(String channel) {
        return request("https://t.me/s/" + channel)
                .map(parser::parseMessages)
                .orElseGet(List::of);
    }

    private List<TgPost> requestMessages(String channel, long beforeId) {
        return request(String.format("https://t.me/s/%s?before=%d", channel, beforeId))
                .map(parser::parseMessages)
                .orElseGet(List::of);
    }

    private Optional<String> request(String url) {
        limiter.acquire();
        return sendGet(url);
    }

    private Optional<String> sendGet(String url) {
        try {
            ConfusingHeaders confusingHeaders = confusingHeadersProvider.provide();
            HttpRequest request = HttpRequest.newBuilder()
                    .header("User-Agent", confusingHeaders.getUniqueUserAgent())
                    .header("Accept", confusingHeaders.getAccept())
                    .uri(URI.create(url))
                    .GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return Optional.ofNullable(response.body());
            } if (response.statusCode() >= 500) {
                log.info("Unsuccessful response for {}: {}. Waiting 3s", url, response.statusCode());
                TimeUnit.SECONDS.sleep(3);
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    return Optional.ofNullable(response.body());
                }
            }
            log.info("Unsuccessful response for {}: {},\n{}", url, response.statusCode(), response.body());
        } catch (Exception e) {
            log.info("Error for request for {}", url, e);
        }
        return Optional.empty();
    }

    @Override
    public void afterPropertiesSet() {
        limiter = RateLimiter.create(scraperConfig.getRequestsPerSecond());
    }
}