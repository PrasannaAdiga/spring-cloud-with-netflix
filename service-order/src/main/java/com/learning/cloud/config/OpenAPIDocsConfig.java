package com.learning.cloud.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class OpenAPIDocsConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        List servers = new ArrayList<Server>();
        servers.add(new Server().url("http://localhost:8093").description("Development server"));

        return new OpenAPI().components(new Components()).info(new Info()
                .description("<p>Provides list of REST APIs for Order</p>")
                .title("API documentation for Order Service").version("1.0.0")).servers(servers);
    }
}
