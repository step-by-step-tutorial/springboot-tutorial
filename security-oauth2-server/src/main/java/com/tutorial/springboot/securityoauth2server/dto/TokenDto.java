package com.tutorial.springboot.securityoauth2server.dto;

import java.time.Instant;

public record TokenDto(String token, Instant expiration) {
}
