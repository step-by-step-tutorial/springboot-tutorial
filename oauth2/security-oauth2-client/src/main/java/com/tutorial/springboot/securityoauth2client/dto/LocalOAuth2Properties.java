package com.tutorial.springboot.securityoauth2client.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@ConfigurationProperties(prefix = "local.oauth2")
@EnableConfigurationProperties
public record LocalOAuth2Properties(String authorizationserver, String resourceserver, String clientapp) {
}