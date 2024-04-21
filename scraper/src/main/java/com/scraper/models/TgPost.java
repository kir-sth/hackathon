package com.scraper.models;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "post")
@Table(
        name = "post",
        indexes = {
                @Index(name = "idx_channel_id", columnList = "channelId", unique = false)
        }
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@IdClass(TgPost.TgPostPK.class)
public class TgPost {

    public static final int MAX_LENGTH_TEXT = 10_000;

    @Id
    Long id;

    @Id
    Long channelId;

    @Nonnull
    String channelName;

    @Nonnull
    PostType type;

    @Nonnull
    @Column(length = MAX_LENGTH_TEXT)
    String text;

    @Nullable
    LocalDateTime moment;

    @Nullable
    Integer viewCount;

    @JsonProperty("link")
    public String getLink() {
        return String.format("https://t.me/%s/%d", channelName, id);
    };

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TgPostPK implements Serializable {
        protected Long id;
        protected Long channelId;
    }
}
