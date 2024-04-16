package scraper.rest;

import java.time.LocalDateTime;
import java.util.List;

import scraper.models.TgChannel;
import scraper.models.TgPost;

public final class MockData {

    public static final TgChannel CHANNEL_1 = TgChannel.builder()
            .title("Канал 1")
            .channelName("channel1")
            .build();

    public static final TgChannel CHANNEL_2 = TgChannel.builder()
            .title("Канал 2")
            .channelName("channel2")
            .build();

    public static final List<TgPost> POSTS_1 = List.of(
            TgPost.builder()
                    .text("text 1")
                    .moment(LocalDateTime.of(2024, 1, 1, 0, 0))
                    .build(),
            TgPost.builder()
                    .text("text 2")
                    .moment(LocalDateTime.of(2024, 1, 2, 0, 0))
                    .build(),
            TgPost.builder()
                    .text("text 3")
                    .moment(LocalDateTime.of(2024, 1, 3, 0, 0))
                    .build(),
            TgPost.builder()
                    .text("text 4")
                    .moment(LocalDateTime.of(2024, 1, 4, 0, 0))
                    .build()
    );

    public static final List<TgPost> POSTS_2 = List.of(
            TgPost.builder()
                    .text("текст 1")
                    .moment(LocalDateTime.of(2024, 2, 1, 0, 0))
                    .build(),
            TgPost.builder()
                    .text("текст 2")
                    .moment(LocalDateTime.of(2024, 2, 2, 0, 0))
                    .build(),
            TgPost.builder()
                    .text("текст 3")
                    .moment(LocalDateTime.of(2024, 2, 3, 0, 0))
                    .build(),
            TgPost.builder()
                    .text("текст 4")
                    .moment(LocalDateTime.of(2024, 2, 4, 0, 0))
                    .build()
    );
}
