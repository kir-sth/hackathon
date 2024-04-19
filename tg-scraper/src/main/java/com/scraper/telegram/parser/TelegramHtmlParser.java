package com.scraper.telegram.parser;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.scraper.models.TgChannel;
import com.scraper.models.TgPost;

public interface TelegramHtmlParser {

    @Nullable
    TgChannel parseChannel(@Nonnull String html);

    @Nonnull
    List<TgPost> parseMessages(@Nonnull String html);
}
