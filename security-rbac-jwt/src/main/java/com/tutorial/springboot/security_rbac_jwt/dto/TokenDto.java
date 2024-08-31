package com.tutorial.springboot.security_rbac_jwt.dto;

import java.util.Date;

public record TokenDto(String token, Date expiration) {
}
