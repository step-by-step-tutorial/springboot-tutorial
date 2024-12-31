package com.tutorial.springboot.securityoauth2server.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangeCredentialsDto(
        @NotBlank(message = "username should not be blank")
        String username,
        @NotBlank(message = "password should not be blank")
        String password,
        @NotBlank(message = "password should not be blank")
        String newPassword
) {
}