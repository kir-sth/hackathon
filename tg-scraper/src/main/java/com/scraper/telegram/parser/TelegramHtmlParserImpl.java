package com.scraper.telegram.parser;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import com.scraper.models.PostType;
import com.scraper.models.TgChannel;
import com.scraper.models.TgPost;

@Slf4j
@Component
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
                            if (descriptionTag.size() == 1) {
                                String description = replaceBrTags(descriptionTag).trim();
                                return TgChannel.builder()
                                        .channelName(name)
                                        .title(title)
                                        .description(description)
                                        .userCnt(userCnt)
                                        .build();
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private boolean isLooksLikeChannel(Document document) {
        Elements channelq = document.getElementsByClass("tgme_page_action tgme_page_context_action");
        return channelq.size() == 1 && channelq.text().contains("Preview channel");
    }

    @Nonnull
    @Override
    public List<TgPost> parseMessages(@Nonnull String html) {
        Document document = getDocument(html);
        return extractChannel(document)
                .map(channel -> parseMessages(document, channel))
                .orElseGet(List::of);
    }

    @Nonnull
    private List<TgPost> parseMessages(Document document, String channel) {
        Date date = new Date();
        return document.getElementsByClass("tgme_widget_message").stream()
                .map(messageWidget -> parseMessage(date, channel, messageWidget))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Nullable
    private TgPost parseMessage(Date date, String channel, Element messageWidget) {
        try {
            Elements textWidget = messageWidget.getElementsByClass("tgme_widget_message_text");
            Integer views = views(messageWidget);
            if (views != null) {
                return TgPost.builder()
                        .id(messageId(messageWidget))
                        .channel(channel)
                        .type(postType(messageWidget))
                        .text(replaceBrTags(textWidget).trim())
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
            return LocalDateTime.from(instant);
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
