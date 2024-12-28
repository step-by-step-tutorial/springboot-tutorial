package com.tutorial.springboot.security_rbac_jwt.fixture.user;

import com.tutorial.springboot.security_rbac_jwt.dto.RoleDto;
import com.tutorial.springboot.security_rbac_jwt.dto.UserDto;

import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public final class UserDtoFixture {

    private UserDtoFixture() {
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
