package scraper.config;

import java.net.http.HttpClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScraperConfiguration {

    @Bean
    public HttpClient httpClient() {
        return HttpClient.newBuilder()
                .build();
    }
}
