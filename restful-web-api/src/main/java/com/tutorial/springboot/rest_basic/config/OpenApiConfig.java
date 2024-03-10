package com.tutorial.springboot.rest_basic.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "Spring Boot Tutorial", description = "RESTful web service"),
        servers = @Server(url = "http://localhost:8080")
)
public class OpenApiConfig {
}
