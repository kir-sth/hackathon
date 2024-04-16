package scraper.services.getchannelposts;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import scraper.services.models.Channel;
import scraper.services.models.Post;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChannelPostsResponse {

    @Nullable
    Channel channel;

    @Nonnull
    List<Post> posts;
}
