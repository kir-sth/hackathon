package com.scraper.rest.services.getchannelposts;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.scraper.models.TgChannel;
import com.scraper.models.TgPost;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChannelPostsResponse {

    @Nullable
    TgChannel channel;

    @Nonnull
    List<TgPost> posts;
}
