package scraper.rest.services.subscribe;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import scraper.models.TgChannel;
import scraper.models.TgUser;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SubscribeRequest {

    @Nonnull
    TgUser user;

    @Nonnull
    TgChannel channel;
}
