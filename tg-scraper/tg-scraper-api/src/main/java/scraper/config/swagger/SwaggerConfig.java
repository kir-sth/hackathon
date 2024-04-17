package scraper.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
//@OpenAPIDefinition()
//public class SwaggerConfig {
//    @Bean
//    public OpenAPI swaggerApiConfig() {
//        var info = new Info()
//                .title("Heroes API")
//                .description("Spring Boot Project to demonstrate SwaggerUI integration")
//                .version("1.0");
//        return new OpenAPI().info(info);
//    }
//}

@Configuration
@OpenAPIDefinition(security = {@SecurityRequirement(name = "bearer-key")})
public class SwaggerConfig {

    @Bean
    public OpenAPI swaggerApiConfig() {
        var info = new Info()
                .title("Heroes API")
                .description("Spring Boot Project to demonstrate SwaggerUI integration")
                .version("1.0");

        var components = new Components()
                .addSecuritySchemes("bearer-key", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));

        return new OpenAPI().components(components).info(info);
    }
}
