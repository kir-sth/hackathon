package scraper.telegram;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import scraper.models.TgChannel;
import scraper.models.TgPost;

public interface TelegramWebClient {

    @Nullable
    TgChannel searchChannel(@Nonnull String channel);

    @Nonnull
    List<TgPost> getLastPosts(@Nonnull String channel, int count);
}
