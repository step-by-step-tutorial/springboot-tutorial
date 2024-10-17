package com.tutorial.springboot.securityoauth2client.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "rsa")
@EnableConfigurationProperties
public record RsaKeyProperties(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
}