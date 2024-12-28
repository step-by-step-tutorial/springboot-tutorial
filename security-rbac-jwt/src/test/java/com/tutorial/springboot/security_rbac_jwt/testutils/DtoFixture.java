package com.tutorial.springboot.security_rbac_jwt.testutils;

import com.tutorial.springboot.security_rbac_jwt.dto.PermissionDto;
import com.tutorial.springboot.security_rbac_jwt.dto.RoleDto;
import com.tutorial.springboot.security_rbac_jwt.dto.UserDto;
import com.tutorial.springboot.security_rbac_jwt.entity.Permission;
import com.tutorial.springboot.security_rbac_jwt.entity.Role;

import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public final class DtoFixture {

    private DtoFixture() {
    }

    public static PermissionDto newGivenPermission() {
        return new PermissionDto()
                .setName("permission")
                .setDescription("permission description")
                .setVersion(0);
    }

    public static PermissionDto newGivenPermission(String name) {
        return new PermissionDto()
                .setName(name)
                .setDescription(name + "description")
                .setVersion(0);
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

    public static UserDto newGivenUser() {
        return new UserDto()
                .setUsername("username")
                .setPassword("password")
                .setEmail("username@email.com")
                .setEnabled(true)
                .setVersion(0);
    }

    public static UserDto newGivenUser(String username) {
        Objects.requireNonNull(username);
        return new UserDto()
                .setUsername(username)
                .setPassword("password")
                .setEmail(username + "@email.com")
                .setEnabled(true)
                .setVersion(0);
    }

    public static UserDto newGivenUser(RoleDto... roles) {
        return new UserDto()
                .setUsername("username")
                .setPassword("password")
                .setEmail("username@email.com")
                .setEnabled(true)
                .setRoles(Stream.of(roles).collect(toList()))
                .setVersion(0);
    }

}
