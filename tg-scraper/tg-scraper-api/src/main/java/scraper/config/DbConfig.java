package scraper.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DbConfig {

    String dbName = System.getenv("DB_NAME");
    String dbDriver = "org.postgresql.Driver";
    String dbHost = System.getenv("DB_HOST");
    String dbPort = System.getenv("DB_PORT");
    String dbUser = System.getenv("DB_USER");
    String dbPass = System.getenv("DB_PASS");
}
