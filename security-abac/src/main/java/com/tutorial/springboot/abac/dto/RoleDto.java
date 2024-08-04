package com.tutorial.springboot.abac.dto;

import java.util.Set;

public record RoleDto(Long id, String authority, Set<PermissionDto> permissions) {
}
