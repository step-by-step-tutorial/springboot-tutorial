package com.tutorial.springboot.securityoauth2server.dto;

import java.time.LocalDateTime;

public record TokenDto(String token, LocalDateTime expiration) {
}
