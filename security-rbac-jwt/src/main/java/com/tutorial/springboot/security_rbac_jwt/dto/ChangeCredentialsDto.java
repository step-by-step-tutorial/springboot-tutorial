package com.tutorial.springboot.security_rbac_jwt.dto;

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