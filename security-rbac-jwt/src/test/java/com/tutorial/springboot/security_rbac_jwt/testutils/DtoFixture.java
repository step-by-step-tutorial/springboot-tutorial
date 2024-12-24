package com.tutorial.springboot.security_rbac_jwt.testutils;

import com.tutorial.springboot.security_rbac_jwt.dto.PermissionDto;
import com.tutorial.springboot.security_rbac_jwt.dto.RoleDto;
import com.tutorial.springboot.security_rbac_jwt.dto.UserDto;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.Objects;
import java.util.stream.Stream;

import static com.tutorial.springboot.security_rbac_jwt.testutils.TestUtils.generateString;
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

    public static PermissionDto givenNullPermission() {
        return null;
    }

    public static RoleDto givenNullRole() {
        return null;
    }

    public static UserDto givenNullUser() {
        return null;
    }


    public static class InvalidPermissions implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of(new PermissionDto()),
                    Arguments.of(new PermissionDto().setName(null).setCreatedBy("unittest").setCreatedAt(now()).setVersion(0)),
                    Arguments.of(new PermissionDto().setName("").setCreatedBy("unittest").setCreatedAt(now()).setVersion(0)),
                    Arguments.of(new PermissionDto().setName(generateString(51)).setCreatedBy("unittest").setCreatedAt(now()).setVersion(0)),
                    Arguments.of(new PermissionDto().setName("permission").setCreatedBy(null).setCreatedAt(now()).setVersion(0)),
                    Arguments.of(new PermissionDto().setName("permission").setCreatedBy("").setCreatedAt(now()).setVersion(0)),
                    Arguments.of(new PermissionDto().setName("permission").setCreatedBy("unittest").setCreatedAt(null).setVersion(0))
            );
        }
    }

    public static class InvalidRoles implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of(new RoleDto()),
                    Arguments.of(new RoleDto().setName(null).setCreatedBy("unittest").setCreatedAt(now()).setVersion(0)),
                    Arguments.of(new RoleDto().setName("").setCreatedBy("unittest").setCreatedAt(now()).setVersion(0)),
                    Arguments.of(new RoleDto().setName(generateString(51)).setCreatedBy("unittest").setCreatedAt(now()).setVersion(0)),
                    Arguments.of(new RoleDto().setName("role").setCreatedBy(null).setCreatedAt(now()).setVersion(0)),
                    Arguments.of(new RoleDto().setName("role").setCreatedBy("").setCreatedAt(now()).setVersion(0)),
                    Arguments.of(new RoleDto().setName("role").setCreatedBy("unittest").setCreatedAt(null).setVersion(0))
            );
        }
    }

    public static class InvalidUsers implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of(new UserDto()),
                    Arguments.of(new UserDto().setUsername(null).setPassword("password").setEmail("username@email.com").setCreatedBy("unittest").setCreatedAt(now()).setVersion(0)),
                    Arguments.of(new UserDto().setUsername("").setPassword("password").setEmail("username@email.com").setCreatedBy("unittest").setCreatedAt(now()).setVersion(0)),
                    Arguments.of(new UserDto().setUsername("username").setPassword(null).setEmail("username@email.com").setCreatedBy("unittest").setCreatedAt(now()).setVersion(0)),
                    Arguments.of(new UserDto().setUsername("username").setPassword("").setEmail("username@email.com").setCreatedBy(null).setCreatedAt(now()).setVersion(0)),
                    Arguments.of(new UserDto().setUsername("username").setPassword("password").setEmail(null).setCreatedBy("").setCreatedAt(now()).setVersion(0)),
                    Arguments.of(new UserDto().setUsername("username").setPassword("password").setEmail("").setCreatedBy("unittest").setCreatedAt(null).setVersion(0))
            );
        }
    }
}
