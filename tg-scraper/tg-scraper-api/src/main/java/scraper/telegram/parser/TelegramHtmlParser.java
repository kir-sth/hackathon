package scraper.telegram.parser;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import scraper.models.TgChannel;
import scraper.models.TgPost;

public interface TelegramHtmlParser {

    @Nullable
    TgChannel parseChannel(@Nonnull String html);

    @Nonnull
    List<TgPost> parseMessages(@Nonnull String html);
}
