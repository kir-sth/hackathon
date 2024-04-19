package com.scraper.models;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "subscription")
@Table(
        name = "subscription",
        indexes = {
                @Index(name = "idx_account_id", columnList = "accountId", unique = false),
                @Index(name = "idx_subscription_channel_id", columnList = "channelId", unique = false)
        }
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TgSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Nonnull
    Long accountId;

    @Nonnull
    Long channelId;
}
