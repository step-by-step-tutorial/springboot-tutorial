package com.tutorial.springboot.securityoauth2client.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@ConfigurationProperties(prefix = "oauth-props")
@EnableConfigurationProperties
public record OauthProperties(String serverUrl, String clientUrl) {
}