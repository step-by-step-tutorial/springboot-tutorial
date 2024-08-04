package com.tutorial.springboot.abac.convertor;

import com.tutorial.springboot.abac.dto.RoleDto;
import com.tutorial.springboot.abac.model.Role;

import java.util.stream.Collectors;

public final class RoleConverter {

    private RoleConverter() {
        // Private constructor to prevent instantiation
    }

    public static RoleDto toDto(Role role) {
        return new RoleDto(role.getId(), role.getAuthority(),
                role.getPermissions().stream().map(PermissionConverter::toDto).collect(Collectors.toSet()));
    }

    public static Role toEntity(RoleDto roleDto) {
        Role role = new Role();
        role.setId(roleDto.id());
        role.setAuthority(roleDto.authority());
        role.setPermissions(roleDto.permissions().stream().map(PermissionConverter::toEntity).collect(Collectors.toSet()));
        return role;
    }
}
