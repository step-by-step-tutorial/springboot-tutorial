package com.tutorial.springboot.rbac.test_utils.stub;

import com.tutorial.springboot.rbac.dto.PermissionDto;
import com.tutorial.springboot.rbac.dto.RoleDto;
import com.tutorial.springboot.rbac.dto.UserDto;
import net.datafaker.Faker;

import java.util.Random;
import java.util.stream.IntStream;

import static com.tutorial.springboot.rbac.test_utils.TestUtils.chooseRandom;

public final class TransientStubFactory {

    private TransientStubFactory() {
    }

    public static StubHelper<PermissionDto> createPermission(int number) {
        var faker = new Faker();
        var array = IntStream.range(0, number)
                .boxed()
                .map(i -> new PermissionDto().setName(faker.hacker().verb()))
                .toArray(PermissionDto[]::new);

        return new StubHelper<>(array);
    }

    public static StubHelper<RoleDto> createRole(int number, int maxPermissionNumber) {
        var faker = new Faker();
        var array = IntStream.range(0, number)
                .boxed()
                .map(i -> new RoleDto()
                        .setName(faker.job().title())
                        .setPermissions(createPermission(chooseRandom(maxPermissionNumber)).asList())
                )
                .toArray(RoleDto[]::new);

        return new StubHelper<>(array);
    }

    public static StubHelper<UserDto> createUser(int number, int maxRoleNumber, int maxPermissionNumber) {
        var faker = new Faker();
        var random = new Random();

        var array = IntStream.range(0, number)
                .boxed()
                .map(i -> new UserDto()
                        .setUsername(faker.name().fullName())
                        .setPassword(faker.internet().password())
                        .setEmail(faker.internet().emailAddress())
                        .setEnabled(true)
                        .setRoles(createRole(chooseRandom(maxRoleNumber), maxPermissionNumber).asList()))
                .toArray(UserDto[]::new);

        return new StubHelper<>(array);
    }
}