package com.scraper.rest.services.getaccountposts;

import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.scraper.models.TgPost;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AccountPostsResponse {

    @Nonnull
    String accountLogin;

    @Nonnull
    Map<String, List<TgPost>> posts;
}
