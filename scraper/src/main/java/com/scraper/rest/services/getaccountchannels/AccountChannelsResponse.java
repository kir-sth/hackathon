package com.scraper.rest.services.getaccountchannels;

import java.util.List;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.scraper.models.TgChannel;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AccountChannelsResponse {

    @Nonnull
    List<TgChannel> channels;
}
