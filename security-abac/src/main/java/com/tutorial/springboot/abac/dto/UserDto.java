package com.tutorial.springboot.abac.dto;

import java.util.Set;

public record UserDto(Long id, String username, String password, boolean enabled, Set<RoleDto> roles) {
}
