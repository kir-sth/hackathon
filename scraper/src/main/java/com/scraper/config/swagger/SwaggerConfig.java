package com.scraper.config.swagger;

import java.util.Optional;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(security = {@SecurityRequirement(name = "bearer-key")})
public class SwaggerConfig {

    @Bean
    public OpenAPI swaggerApiConfig() {
        var info = new Info()
                .title("tg scraper")
                .version("1.0");

        var components = new Components()
                .addSecuritySchemes("bearer-key", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));

        return new OpenAPI().components(components).info(info);
    }

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("default")
                .addOpenApiCustomizer(serverCustomizer())
                .build();
    }

    @Bean
    public OpenApiCustomizer serverCustomizer() {
        Optional<String> serverUrlForSwagger = Optional.ofNullable(System.getenv("SERVER_URL_FOR_SWAGGER"));
        if (serverUrlForSwagger.isPresent()) {
            return openApi -> {
                openApi.getServers().clear();
                openApi.getServers().add(new Server().url(serverUrlForSwagger.get()).description("Production server"));
            };
        }
        return openApi -> {};
    }
}
