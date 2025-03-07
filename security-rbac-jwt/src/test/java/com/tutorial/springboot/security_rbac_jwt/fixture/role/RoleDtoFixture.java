package com.tutorial.springboot.security_rbac_jwt.fixture.role;

import com.tutorial.springboot.security_rbac_jwt.dto.PermissionDto;
import com.tutorial.springboot.security_rbac_jwt.dto.RoleDto;

import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public final class RoleDtoFixture {

    private RoleDtoFixture() {
    }

    public static RoleDto newGivenRole() {
        return new RoleDto()
                .setName("role")
                .setDescription("role description")
                .setVersion(0);
    }

    public static RoleDto newGivenRole(String name) {
        return new RoleDto()
                .setName(name)
                .setDescription(name + "description")
                .setVersion(0);
    }

    public static RoleDto newGivenRole(PermissionDto... permissions) {
        return newGivenRole()
                .setPermissions(Stream.of(permissions).collect(toList()));
    }
}
