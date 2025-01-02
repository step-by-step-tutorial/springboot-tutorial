package com.tutorial.springboot.security_rbac_jwt.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@ConfigurationProperties(prefix = "secure-resource")
@EnableConfigurationProperties
public record SecureProperties(
        String homeUrl,
        String[] unsecureUrls,
        String[] corsOriginUrls,
        String[] corsHttpMethods,
        String[] corsHttpHeaders,
        String corsBasePath
) {

}
