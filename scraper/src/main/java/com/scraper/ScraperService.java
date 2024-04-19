package com.scraper;

import java.util.List;
import java.util.Optional;

import javax.annotation.ParametersAreNonnullByDefault;

import com.scraper.dao.ChannelDao;
import com.scraper.dao.PostDao;
import com.scraper.models.PostType;
import com.scraper.models.TgChannel;
import com.scraper.models.TgPost;
import com.scraper.telegram.TelegramWebClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@ParametersAreNonnullByDefault
@RequiredArgsConstructor
public class ScraperService {

    private final TelegramWebClient webClient;
    private final ChannelDao channelDao;
    private final PostDao postDao;
    private static final int INITIALIZATION_POST_COUNT = Optional.ofNullable(System.getenv("INITIALIZATION_POST_COUNT"))
            .map(Integer::parseInt)
            .orElse(100);

    private static final int CONTINUE_POST_COUNT = Optional.ofNullable(System.getenv("CONTINUE_POST_COUNT"))
            .map(Integer::parseInt)
            .orElse(10000);

    public void scrapAll() {
        List<TgChannel> channels = channelDao.findWithSubscribers();
        channels.forEach(this::scrap);
    }

    public void scrap(TgChannel channel) {
        log.info("Scraping for {} started", channel.getChannelName());
        try {
            TgChannel webChannel = webClient.searchChannel(channel.getChannelName());
            if (webChannel != null) {
                List<TgPost> posts = channel.getLastPostId() == null
                        ? webClient.getLastPosts(channel, INITIALIZATION_POST_COUNT)
                        : webClient.getPostsAfterPostId(channel, channel.getLastPostId(), CONTINUE_POST_COUNT);
                posts = posts.stream()
                        .filter(p -> PostType.Text.equals(p.getType()))
                        .toList();
                postDao.saveAllAndFlush(posts);

                Long lastPostId = posts.stream()
                        .map(TgPost::getId)
                        .max(Long::compareTo)
                        .orElse(null);
                webChannel = webChannel.toBuilder()
                        .id(channel.getId())
                        .lastPostId(lastPostId)
                        .build();
                channelDao.saveAndFlush(webChannel);
                log.info("Scraping for {} finished", channel.getChannelName());
            }
        } catch (Throwable ex) {
            log.error("Error scraping channel {}", channel.getChannelName(), ex);
        }
    }
}
