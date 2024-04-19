package com.scraper.rest.services.subscribe;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.scraper.models.TgUser;
import lombok.Data;
import com.scraper.models.TgChannel;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SubscribeRequest {

    @Nonnull
    TgUser user;

    @Nonnull
    TgChannel channel;
}
