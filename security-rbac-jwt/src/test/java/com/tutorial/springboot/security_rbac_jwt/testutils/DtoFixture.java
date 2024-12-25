package com.tutorial.springboot.security_rbac_jwt.testutils;

import com.tutorial.springboot.security_rbac_jwt.dto.PermissionDto;
import com.tutorial.springboot.security_rbac_jwt.dto.RoleDto;
import com.tutorial.springboot.security_rbac_jwt.dto.UserDto;

import java.util.Objects;

import static java.time.LocalDateTime.now;

public final class DtoFixture {

    private DtoFixture() {
    }

    public static UserDto newGivenUser() {
        return new UserDto()
                .setUsername("username").setPassword("password").setEmail("username@email.com").setEnabled(true)
                .setCreatedBy("unittest").setCreatedAt(now())
                .setVersion(0);
    }

    public static UserDto newGivenUser(String username) {
        Objects.requireNonNull(username);
        return new UserDto()
                .setUsername(username).setPassword("password").setEmail(username + "@email.com").setEnabled(true)
                .setCreatedBy("unittest").setCreatedAt(now())
                .setVersion(0);
    }

    public static RoleDto newGivenRole() {
        return new RoleDto()
                .setName("role")
                .setCreatedBy("unittest").setCreatedAt(now())
                .setVersion(0);
    }

    public static RoleDto newGivenRole(String name) {
        return new RoleDto()
                .setName(name)
                .setCreatedBy("unittest").setCreatedAt(now())
                .setVersion(0);
    }

    public static PermissionDto newGivenPermission() {
        return new PermissionDto()
                .setName("permission")
                .setCreatedBy("unittest").setCreatedAt(now())
                .setVersion(0);
    }

    public static PermissionDto newGivenPermission(String name) {
        return new PermissionDto()
                .setName(name)
                .setCreatedBy("unittest").setCreatedAt(now())
                .setVersion(0);
    }
}
