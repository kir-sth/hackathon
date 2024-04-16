package scraper;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    @SneakyThrows
    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);

//        JerseyLoggingStarter.start();

//        final AnnotationConfigApplicationContext ctx =
//                new AnnotationConfigApplicationContext(SearcherConfiguration.class);
//        ctx.registerShutdownHook();
//
//        final ResourceConfig resourceConfig = new BaseApiJerseyConfig(List.of("searcher"))
//                .property("contextConfig", ctx)
//                .setApplicationName("audio-search")
//                .registerClasses(ApiResource.class);
//
//        ServerStarter.startServerOrFail(resourceConfig);
    }
}
