package com.scraper.rest.services.getaccountchannels;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AccountChannelsRequest {

    @Nullable
    String accountId;

    @Nullable
    String accountLogin;
}
