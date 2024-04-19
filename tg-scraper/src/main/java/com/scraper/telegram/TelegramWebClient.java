package com.scraper.telegram;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.scraper.models.TgChannel;
import com.scraper.models.TgPost;

public interface TelegramWebClient {

    @Nullable
    TgChannel searchChannel(@Nonnull String channel);

    @Nonnull
    List<TgPost> getLastPosts(@Nonnull String channel, int count);
}
