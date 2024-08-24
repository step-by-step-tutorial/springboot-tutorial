package com.tutorial.springboot.rbac.test_utils.stub;

import com.tutorial.springboot.rbac.entity.Permission;
import com.tutorial.springboot.rbac.entity.Role;
import com.tutorial.springboot.rbac.entity.User;
import net.datafaker.Faker;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static com.tutorial.springboot.rbac.test_utils.TestUtils.chooseRandom;

public final class EntityStubFactory {

    private EntityStubFactory() {
    }

    public static StubHelper<Permission> createPermission(int number) {
        var faker = new Faker();
        var array = IntStream.range(0, number)
                .boxed()
                .map(i -> new Permission()
                        .setName(faker.hacker().verb())
                        .setCreatedBy("admin")
                        .setCreatedAt(LocalDateTime.now())
                        .setVersion(1)
                )
                .toArray(Permission[]::new);

        return new StubHelper<>(array);
    }

    public static StubHelper<Role> createRole(int number, int randomPermissionNumber) {
        var faker = new Faker();
        var array = IntStream.range(0, number)
                .boxed()
                .map(i -> new Role()
                        .setName(faker.job().title())
                        .setCreatedBy("admin")
                        .setCreatedAt(LocalDateTime.now())
                        .setVersion(1)
                        .setPermissions(createPermission(chooseRandom(randomPermissionNumber)).asList()))
                .toArray(Role[]::new);

        return new StubHelper<>(array);
    }

    public static StubHelper<User> createUser(int number, int randomRoleNumber, int randomPermissionNumber) {
        var faker = new Faker();
        var array = IntStream.range(0, number)
                .boxed()
                .map(i -> new User()
                        .setUsername(faker.name().fullName())
                        .setPassword(faker.internet().password())
                        .setEmail(faker.internet().emailAddress())
                        .setEnabled(true)
                        .setCreatedBy("admin")
                        .setCreatedAt(LocalDateTime.now())
                        .setVersion(1)
                        .setRoles(createRole(chooseRandom(randomRoleNumber), randomPermissionNumber).asList()))
                .toArray(User[]::new);

        return new StubHelper<>(array);

    }
}