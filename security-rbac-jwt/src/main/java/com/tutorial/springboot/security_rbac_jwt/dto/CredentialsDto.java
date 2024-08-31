package com.tutorial.springboot.security_rbac_jwt.dto;

import jakarta.validation.constraints.NotBlank;

public record CredentialsDto(
        @NotBlank(message = "username should not be blank")
        String username,
        @NotBlank(message = "password should not be blank")
        String password
) {
}