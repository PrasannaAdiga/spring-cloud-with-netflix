package com.learning.cloud.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@SecurityScheme(
        name = "BasicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
public class OpenAPIDocsConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        List servers = new ArrayList<Server>();
        servers.add(new Server().url("http://localhost:8090").description("Development server"));

        return new OpenAPI().components(new Components()).info(new Info()
                .description("<p>Provides list of REST APIs for User Account</p>")
                .title("API documentation for Account Service").version("1.0.0")).servers(servers);
    }
}
