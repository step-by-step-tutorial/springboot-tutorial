package com.tutorial.springboot.securityoauth2client.dto;

import java.time.LocalDateTime;

public record TokenDto(String token, LocalDateTime expiration) {
}
