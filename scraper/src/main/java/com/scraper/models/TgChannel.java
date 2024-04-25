package com.scraper.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.scraper.models.TgPost.MAX_LENGTH_TEXT;
import static com.scraper.models.TgPost.MAX_LENGTH_TITLE;

@Data
@Entity(name = "channel")
@Table(name = "channel",
        uniqueConstraints = @UniqueConstraint(columnNames = {"channelName"})
)
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TgChannel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Nonnull
    Long id;

    @Nullable
    @Column(length = MAX_LENGTH_TITLE)
    String title;

    @Nullable
    @Column(length = MAX_LENGTH_TEXT)
    String description;

    @Nullable
    Integer userCnt;

    @Nonnull
    String channelName;

    @Nonnull
    Long lastPostId;
}
