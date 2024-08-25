package com.tutorial.springboot.rbac.dto;

import java.util.Date;

public record LoginResponse(String token, Date expiresIn) {
}
