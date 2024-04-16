package scraper.config;

import java.util.Optional;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ScraperConfig {

    int requestsPerSecond = Optional.ofNullable(System.getenv("REQUESTS_PER_SECOND"))
            .map(Integer::parseInt)
            .orElse(5);
}
