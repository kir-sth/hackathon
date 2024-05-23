package com.scraper.telegram;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.google.common.util.concurrent.RateLimiter;
import com.scraper.config.ScraperConfig;
import com.scraper.models.TgChannel;
import com.scraper.models.TgPost;
import com.scraper.telegram.parser.TelegramHtmlParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ParametersAreNonnullByDefault
@SuppressWarnings("UnstableApiUsage")
public class TelegramWebClientImpl implements TelegramWebClient, InitializingBean {

    private static final Comparator<TgPost> COMPARATOR = Comparator.comparing(TgPost::getId, Comparator.reverseOrder());

    private final TelegramHtmlParser parser;
    private final HttpClient client;
    private final ScraperConfig scraperConfig;
    private RateLimiter limiter;



    @Override
    @Nullable
    public TgChannel searchChannel(@Nonnull String channel) {
        return request("https://t.me/" + channel)
                .map(parser::parseChannel)
                .orElse(null);
    }

    @Nonnull
    @Override
    public List<TgPost> getLastPosts(TgChannel channel, int count) {
        return getPosts(
                channel.getId(),
                channel.getChannelName(),
                requestMessages(channel.getId(), channel.getChannelName()),
                count
        );
    }

    @Nonnull
    @Override
    public List<TgPost> getPostsBeforePostId(TgChannel channel, long beforePostId, int count) {
        return getPosts(
                channel.getId(),
                channel.getChannelName(),
                requestBeforePosts(channel.getId(), channel.getChannelName(), beforePostId),
                count
        );
    }

    @Nonnull
    @Override
    public List<TgPost> getPostsAfterPostId(TgChannel channel, long afterPostId, int count) {
        return getPosts(
                channel.getId(),
                channel.getChannelName(),
                requestAfterPosts(channel.getId(), channel.getChannelName(), afterPostId),
                count
        );
    }

    @Nonnull
    private List<TgPost> getPosts(Long channelId, String channelName, List<TgPost> initMessages, int count) {
        Set<TgPost> result = new TreeSet<>(COMPARATOR);
        List<TgPost> posts = initMessages;
        while (!posts.isEmpty() && result.size() < count) {
            result.addAll(posts);
            posts = requestAfterPosts(channelId, channelName, posts.get(0).getId());
        }
        return new ArrayList<>(result);
    }

    private List<TgPost> requestMessages(Long channelId, String channelName) {
        return request("https://t.me/s/" + channelName)
                .map(html -> parser.parseMessages(channelId, html))
                .orElseGet(List::of);
    }

    private List<TgPost> requestBeforePosts(Long channelId, String channelName, long beforeId) {
        return request(String.format("https://t.me/s/%s?before=%d", channelName, beforeId))
                .map(html -> parser.parseMessages(channelId, html))
                .orElseGet(List::of);
    }

    private List<TgPost> requestAfterPosts(Long channelId, String channelName, long afterId) {
        return request(String.format("https://t.me/s/%s?after=%d", channelName, afterId))
                .map(html -> {
                    try {
                        return parser.parseMessages(channelId, html);
                    } catch (Throwable ex) {
                        log.error(ex.getMessage(), ex);
                        return null;
                    }
                })
                .orElseGet(List::of)
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Optional<String> request(String url) {
        limiter.acquire();
        return sendGet(url);
    }

    private Optional<String> sendGet(String url) {
        try {
            ConfusingHeaders confusingHeaders = ConfusingHeadersProvider.provide();
            HttpRequest request = HttpRequest.newBuilder()
                    .header("User-Agent", confusingHeaders.getUniqueUserAgent())
                    .header("Accept", confusingHeaders.getAccept())
                    .uri(URI.create(url))
                    .GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return Optional.ofNullable(response.body());
            } if (response.statusCode() >= 500) {
                log.info("Unsuccessful response for {}: {}. Waiting 3s", url, response.statusCode());
                TimeUnit.SECONDS.sleep(3);
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    return Optional.ofNullable(response.body());
                }
            }
            log.info("Unsuccessful response for {}: {},\n{}", url, response.statusCode(), response.body());
        } catch (Exception e) {
            log.info("Error for request for {}", url, e);
        }
        return Optional.empty();
    }

    @Override
    public void afterPropertiesSet() {
        limiter = RateLimiter.create(scraperConfig.getRequestsPerSecond());
    }
}