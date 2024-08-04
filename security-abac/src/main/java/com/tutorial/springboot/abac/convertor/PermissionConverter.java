package com.tutorial.springboot.abac.convertor;

import com.tutorial.springboot.abac.dto.PermissionDto;
import com.tutorial.springboot.abac.model.Permission;

public final class PermissionConverter {

    private PermissionConverter() {
        // Private constructor to prevent instantiation
    }

    public static PermissionDto toDto(Permission permission) {
        return new PermissionDto(permission.getId(), permission.getName());
    }

    public static Permission toEntity(PermissionDto permissionDto) {
        Permission permission = new Permission();
        permission.setId(permissionDto.id());
        permission.setName(permissionDto.name());
        return permission;
    }
}
