package scraper;

import java.util.Optional;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

@Component
public class ChangePort implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        int port = Optional.ofNullable(System.getenv("PORT"))
                .map(Integer::parseInt)
                .orElse(8080);
        factory.setPort(port);
    }
}