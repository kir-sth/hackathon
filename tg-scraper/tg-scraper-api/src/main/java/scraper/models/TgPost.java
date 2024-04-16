package scraper.models;

import java.time.LocalDateTime;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TgPost {

    @Nonnull
    Long id;

    @Nonnull
    String channel;

    @Nonnull
    PostType type;

    @Nonnull
    String text;

    @Nullable
    LocalDateTime moment;

    @Nullable
    Integer viewCount;
}
