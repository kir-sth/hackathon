package scraper.rest.services.getuserchannels;

import java.util.List;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import scraper.models.TgChannel;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserChannelsResponse {

    @Nonnull
    List<TgChannel> channels;
}
