package scraper.services.getchannelposts;

import java.time.LocalDateTime;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import scraper.services.models.Channel;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChannelPostsRequest {

    @Nonnull
    Channel channel;

    @Nullable
    LocalDateTime dateFrom;

    @Nullable
    LocalDateTime dateTo;

    @Nullable
    Integer limit;
}
