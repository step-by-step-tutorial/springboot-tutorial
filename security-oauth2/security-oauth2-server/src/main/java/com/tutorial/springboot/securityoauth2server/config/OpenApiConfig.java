package com.tutorial.springboot.securityoauth2server.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "Spring Boot Tutorial", description = "OAuth 2.0 service"),
        servers = @Server(url = "http://localhost:8080"),
        security = {@SecurityRequirement(name = "openapiauth")}
)

public class OpenApiConfig {

    @Profile("openapibasic")
    @Configuration
    @SecurityScheme(name = "openapiauth",
            description = "Basic Authentication",
            scheme = "basic",
            type = SecuritySchemeType.HTTP,
            in = SecuritySchemeIn.HEADER)
    class BasicAuth {

    }

    @Profile("openapijwt")
    @Configuration
    @SecurityScheme(
            name = "openapiauth",
            description = "JWT Authentication",
            scheme = "bearer",
            type = SecuritySchemeType.HTTP,
            bearerFormat = "JWT",
            in = SecuritySchemeIn.HEADER
    )
    class JwtAuth {

    }
}
