package com.tutorial.springboot.security_rbac_jwt.testutils;

import com.tutorial.springboot.security_rbac_jwt.entity.Permission;
import com.tutorial.springboot.security_rbac_jwt.entity.Role;
import com.tutorial.springboot.security_rbac_jwt.entity.User;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.Objects;
import java.util.stream.Stream;

import static com.tutorial.springboot.security_rbac_jwt.testutils.TestUtils.generateString;

public final class EntityFixture {

    private EntityFixture() {
    }

    public static User newGivenUser() {
        return new User()
                .setUsername("username")
                .setPassword("password")
                .setEmail("username@email.com")
                .setEnabled(true)
                .setVersion(0);
    }

    public static User newGivenUser(String username) {
        Objects.requireNonNull(username);
        return new User()
                .setUsername(username)
                .setPassword("password")
                .setEmail(username + "@email.com")
                .setEnabled(true)
                .setVersion(0);
    }

    public static Role newGivenRole() {
        return new Role()
                .setName("role")
                .setDescription("role description")
                .setVersion(0);
    }

    public static Role newGivenRole(String name) {
        return new Role()
                .setName(name)
                .setDescription("role description")
                .setVersion(0);
    }

    public static Permission newGivenPermission() {
        return new Permission()
                .setName("permission")
                .setDescription("permission description")
                .setVersion(0);
    }

    public static Permission newGivenPermission(String name) {
        return new Permission()
                .setName(name)
                .setDescription("permission description")
                .setVersion(0);
    }

    public static class InvalidPermissions implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of(new Permission()),
                    Arguments.of(new Permission().setName(null)),
                    Arguments.of(new Permission().setName("")),
                    Arguments.of(new Permission().setName(generateString(51)))
            );
        }
    }

    public static class InvalidRoles implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of(new Role()),
                    Arguments.of(new Role().setName(null)),
                    Arguments.of(new Role().setName("")),
                    Arguments.of(new Role().setName(generateString(51)))
            );
        }
    }

    public static class InvalidUsers implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of(new User()),
                    Arguments.of(new User().setUsername(null)),
                    Arguments.of(new User().setUsername("").setPassword("password").setEmail("username@email.com")),
                    Arguments.of(new User().setUsername("username").setPassword(null).setEmail("username@email.com")),
                    Arguments.of(new User().setUsername("username").setPassword("").setEmail("username@email.com")),
                    Arguments.of(new User().setUsername("username").setPassword("password").setEmail(null)),
                    Arguments.of(new User().setUsername("username").setPassword("password").setEmail(""))
            );
        }
    }
}
