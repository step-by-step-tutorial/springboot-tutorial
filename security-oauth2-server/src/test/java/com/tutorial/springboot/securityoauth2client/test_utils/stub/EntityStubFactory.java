package com.tutorial.springboot.securityoauth2client.test_utils.stub;

import com.tutorial.springboot.securityoauth2client.entity.*;
import com.tutorial.springboot.securityoauth2client.enums.GrantType;
import net.datafaker.Faker;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static com.tutorial.springboot.securityoauth2client.test_utils.TestUtils.chooseRandom;

public final class EntityStubFactory {

    private EntityStubFactory() {
    }

    public static StubHelper<Client> createClientOfJwtBearer(int number) {
        var faker = new Faker();
        var array = IntStream.range(0, number)
                .boxed()
                .map(i -> new Client()
                        .setClientId(faker.app().name())
                        .setClientSecret(faker.internet().password())
                        .setRedirectUri(faker.internet().url())
                        .setGrantTypes(List.of(GrantType.JWT_BEARER.name()))
                        .setScopes(Arrays.asList("read", "write")
                                .stream()
                                .map(s -> new Scope().setName(s))
                                .toList())
                        .setAccessTokenValiditySeconds(3600)
                        .setRefreshTokenValiditySeconds(1209600))
                .toArray(Client[]::new);

        return new StubHelper<>(array);
    }

    public static StubHelper<Permission> createPermission(int number) {
        var faker = new Faker();
        var array = IntStream.range(0, number)
                .boxed()
                .map(i -> new Permission()
                        .setName(faker.hacker().verb())
                        .setCreatedBy("test")
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
                        .setCreatedBy("test")
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
                        .setCreatedBy("test")
                        .setCreatedAt(LocalDateTime.now())
                        .setVersion(1)
                        .setRoles(createRole(chooseRandom(randomRoleNumber), randomPermissionNumber).asList()))
                .toArray(User[]::new);

        return new StubHelper<>(array);

    }
}