package com.scraper.telegram.parser;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.scraper.models.PostType;
import com.scraper.models.TgChannel;
import com.scraper.models.TgPost;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import static com.scraper.models.TgPost.MAX_LENGTH_TEXT;
import static com.scraper.models.TgPost.MAX_LENGTH_TITLE;

@Slf4j
@Component
@ParametersAreNonnullByDefault
public class TelegramHtmlParserImpl implements TelegramHtmlParser {

    @Nullable
    @Override
    public TgChannel parseChannel(@Nonnull String html) {
        Document document = getDocument(html);
        Elements titleTag = document.getElementsByTag("title");
        Elements channelTitleTag = document.getElementsByClass("tgme_page_title");
        Elements usersTag = document.getElementsByClass("tgme_page_extra");
        Elements descriptionTag = document.getElementsByClass("tgme_page_description");


        if(isLooksLikeChannel(document)) {
            if (titleTag.size() == 1) {
                String name = titleTag.text().replaceFirst("^.*@", "").trim();
                if (channelTitleTag.size() == 1) {
                    String title = channelTitleTag.text().trim();
                    if (usersTag.size() == 1) {
                        String usersStr = usersTag.text().replaceAll("\\D", "");
                        if (!usersStr.isBlank()) {
                            int userCnt = Integer.parseInt(usersStr);
                            return TgChannel.builder()
                                    .channelName(name)
                                    .title(cutTitleIfNeed(title))
                                    .description(buildDescription(descriptionTag))
                                    .userCnt(userCnt)
                                    .build();
                        }
                    }
                }
            }
        }
        return null;
    }

    @Nullable
    private String buildDescription(Elements descriptionTag) {
        if (descriptionTag.size() == 1) {
            return cutTextIfNeed(replaceBrTags(descriptionTag).trim());
        }
        return null;
    }

    private boolean isLooksLikeChannel(Document document) {
        Elements channelq = document.getElementsByClass("tgme_page_context_link");
        return channelq.size() == 1 && channelq.text().contains("Preview channel");
    }

    @Nonnull
    @Override
    public List<TgPost> parseMessages(Long channelId, @Nonnull String html) {
        Document document = getDocument(html);
        return extractChannel(document)
                .map(channelName -> parseMessages(document, channelId, channelName))
                .orElseGet(List::of);
    }

    @Nonnull
    private List<TgPost> parseMessages(Document document, Long channelId, String channelName) {
        Date date = new Date();
        return document.getElementsByClass("tgme_widget_message").stream()
                .map(messageWidget -> parseMessage(date, channelId, channelName, messageWidget))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Nullable
    private TgPost parseMessage(Date date, Long channelId, String channel, Element messageWidget) {
        try {
            Elements textWidget = messageWidget.getElementsByClass("tgme_widget_message_text");
            Integer views = views(messageWidget);
            if (views != null) {
                return TgPost.builder()
                        .id(messageId(messageWidget))
                        .channelId(channelId)
                        .channelName(channel)
                        .type(postType(messageWidget))
                        .text(cutTextIfNeed(replaceBrTags(textWidget).trim()))
                        .moment(publishDate(messageWidget))
                        .viewCount(views)
                        .build();
            }
            return null;
        } catch (RuntimeException e) {
            log.info(messageWidget.html());
            throw e;
        }
    }

    private String cutTitleIfNeed(String title) {
        return title.length() > MAX_LENGTH_TITLE ? title.substring(0, MAX_LENGTH_TITLE) : title;
    }

    private String cutTextIfNeed(String text) {
        return text.length() > MAX_LENGTH_TEXT ? text.substring(0, MAX_LENGTH_TEXT) : text;
    }

    private Optional<String> extractChannel(@Nonnull Document document) {
        Elements usernameTag = document.getElementsByClass("tgme_channel_info_header_username");
        if (usernameTag.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(usernameTag.text().substring(1));
    }

    private long messageId(@Nonnull Element messageWidget) {
        String messageIdStr = messageWidget.attributes().get("data-post").replaceFirst("^.*?/", "");
        return Long.parseLong(messageIdStr);
    }

    @Nonnull
    private PostType postType(@Nonnull Element postWidget) {
        Elements videos = postWidget.getElementsByClass("tgme_widget_message_video");
        Elements photos = postWidget.getElementsByClass("tgme_widget_message_photo");
        Elements text = postWidget.getElementsByClass("tgme_widget_message_text");
        Elements document = postWidget.getElementsByClass("tgme_widget_message_document");
        Elements audio = postWidget.getElementsByClass("tgme_widget_message_audio");

        if (videos.isEmpty() && photos.isEmpty() && !text.isEmpty()) {
            return PostType.Text;
        } else if (!videos.isEmpty() && !photos.isEmpty()) {
            return PostType.Multimedia;
        } else if (!videos.isEmpty()) {
            return PostType.Video;
        } else if (!photos.isEmpty()) {
            return PostType.Photo;
        } else if (!document.isEmpty()) {
            return PostType.Document;
        } else if (!audio.isEmpty()) {
            return PostType.Audio;
        }
        return PostType.Other;
    }

    @Nullable
    private LocalDateTime publishDate(@Nonnull Element postWidget) {
        Elements postInfoTag = postWidget.getElementsByClass("tgme_widget_message_info");
        Elements timeTag = postInfoTag.select("time");
        if (timeTag.size() == 1) {
            String datetimeAttribute = timeTag.get(0).attributes().get("datetime");
            Instant instant = OffsetDateTime.parse(datetimeAttribute).toInstant();
            return LocalDateTime.ofEpochSecond(instant.getEpochSecond(), 0, ZoneOffset.UTC);
        }
        return null;
    }

    @Nullable
    private Integer views(@Nonnull Element postWidget) {
        Elements viewsTag = postWidget.getElementsByClass("tgme_widget_message_views");
        if (!viewsTag.isEmpty()) {
            String text = viewsTag.text();
            double k = 1.0;
            if (text.endsWith("K")) {
                k = 1000;
            } else if (text.endsWith("M")) {
                k = 1_000_000;
            }
            return (int) (Double.parseDouble(text.replaceAll("[MK]", "")) * k);
        }
        return null;
    }

    @Nonnull
    private Document getDocument(@Nonnull String html) {
        Document document = Jsoup.parse(html);
        document.outputSettings(new Document.OutputSettings().prettyPrint(false));
        return document;
    }

    @Nonnull
    private String replaceBrTags(@Nonnull Elements elements) {
        elements.select("br").append("\\n");
        return elements.text().replaceAll("\\\\n", "\n");
    }
}
