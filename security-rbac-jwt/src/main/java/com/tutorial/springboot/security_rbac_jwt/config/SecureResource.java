package com.tutorial.springboot.security_rbac_jwt.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@ConfigurationProperties(prefix = "secure-resource")
@EnableConfigurationProperties
public record SecureResource(
        String homeUrl,
        String[] unsecureUrls,
        String[] corsOriginUrls,
        String[] corsHttpMethods,
        String[] corsHttpHeaders,
        String basePath
) {

}
