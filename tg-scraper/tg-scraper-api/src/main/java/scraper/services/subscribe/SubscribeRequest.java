package scraper.services.subscribe;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import scraper.services.models.Channel;
import scraper.services.models.User;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SubscribeRequest {

    @Nonnull
    User user;

    @Nonnull
    Channel channel;
}
