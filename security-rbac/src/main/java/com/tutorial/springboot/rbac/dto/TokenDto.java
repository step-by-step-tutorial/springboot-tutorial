package com.tutorial.springboot.rbac.dto;

import java.util.Date;

public record TokenDto(String token, Date expiration) {
}
