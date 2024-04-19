package com.scraper.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
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

@Data
@Entity(name = "account")
@Table(name = "account",
        uniqueConstraints = @UniqueConstraint(columnNames = {"login"})
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TgAccount {

    @Id
//    @SequenceGenerator(name = "UserSequence", sequenceName = "\"USER_SEQ\"", allocationSize = 1)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UserSequence")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Nullable
    String title;

    @Nonnull
    String login;
}
