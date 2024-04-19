package com.scraper.rest.services.getaccountposts;

import java.time.LocalDateTime;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.scraper.models.TgChannel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AccountlPostsRequest {

    @Nonnull
    TgChannel channel;

    @Nullable
    LocalDateTime dateFrom;

    @Nullable
    LocalDateTime dateTo;

    @Nullable
    Integer limit;
}
