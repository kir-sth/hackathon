package com.scraper.telegram;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.scraper.models.TgChannel;
import com.scraper.models.TgPost;

@ParametersAreNonnullByDefault
public interface TelegramWebClient {

    @Nullable
    TgChannel searchChannel(String channel);

    @Nonnull
    List<TgPost> getLastPosts(TgChannel channel, int count);

    @Nonnull
    List<TgPost> getPostsBeforePostId(TgChannel channel, long beforeId, int count);

    @Nonnull
    List<TgPost> getPostsAfterPostId(TgChannel channel, long afterId, int count);
}
