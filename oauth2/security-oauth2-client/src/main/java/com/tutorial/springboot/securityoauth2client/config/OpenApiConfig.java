package com.tutorial.springboot.securityoauth2client.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "Spring Boot Tutorial", description = "OAuth Client"),
        servers = @Server(url = "http://localhost:9000")
)
public class OpenApiConfig {
}
