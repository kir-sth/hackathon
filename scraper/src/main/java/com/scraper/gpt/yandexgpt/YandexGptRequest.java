package com.scraper.gpt.yandexgpt;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
@Builder
public class YandexGptRequest implements Serializable {
  private String modelUri;

  @Singular
  private List<Message> messages;

  private CompletionOptions completionOptions;

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
  @Builder
  public static class Message implements Serializable {
    private String role;

    private String text;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
  @Builder
  public static class CompletionOptions implements Serializable {
    private Boolean stream;

    private Double temperature;

    private String maxTokens;
  }
}
