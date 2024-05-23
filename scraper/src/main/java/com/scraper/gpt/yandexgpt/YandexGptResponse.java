package com.scraper.gpt.yandexgpt;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
@Builder
public class YandexGptResponse implements Serializable {
  private Result result;

  public List<String> getCategories(){
    return List.of();
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
  @Builder
  public static class Result implements Serializable {
    private String modelVersion;

    private Usage usage;

    private List<Alternatives> alternatives;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
    @Builder
    public static class Usage implements Serializable {
      private String inputTextTokens;

      private String totalTokens;

      private String completionTokens;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
    @Builder
    public static class Alternatives implements Serializable {
      private Message message;

      private String status;

      @Data
      @AllArgsConstructor
      @NoArgsConstructor
      @JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
      @Builder
      public static class Message implements Serializable {
        private String role;

        private String text;
      }
    }
  }
}
