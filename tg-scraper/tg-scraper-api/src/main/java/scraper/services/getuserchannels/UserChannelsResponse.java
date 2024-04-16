package scraper.services.getuserchannels;

import java.util.List;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import scraper.services.models.Channel;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserChannelsResponse {

    @Nonnull
    List<Channel> channels;
}
