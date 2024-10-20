package com.tutorial.springboot.securityoauth2client.test_utils.stub;

import com.tutorial.springboot.securityoauth2client.dto.ClientDto;
import com.tutorial.springboot.securityoauth2client.dto.PermissionDto;
import com.tutorial.springboot.securityoauth2client.dto.RoleDto;
import com.tutorial.springboot.securityoauth2client.dto.UserDto;
import com.tutorial.springboot.securityoauth2client.enums.GrantType;
import net.datafaker.Faker;

import java.util.Arrays;
import java.util.stream.IntStream;

import static com.tutorial.springboot.securityoauth2client.test_utils.TestUtils.chooseRandom;

public final class DtoStubFactory {

    private DtoStubFactory() {
    }

    public static StubHelper<ClientDto> createClientOfJwtBearer(int number) {
        var faker = new Faker();
        var array = IntStream.range(0, number)
                .boxed()
                .map(i -> faker.app().name().replace(" ", "-").toLowerCase() + "-client")
                .map(name -> new ClientDto()
                        .setClientId(name)
                        .setClientSecret(faker.internet().password())
                        .setRedirectUri("http://localhost:9000/login/oauth2/code/" + name)
                        .setGrantTypes(GrantType.allType())
                        .setScopes(Arrays.asList("read", "write"))
                        .setAccessTokenValiditySeconds(3600)
                        .setRefreshTokenValiditySeconds(1209600))
                .toArray(ClientDto[]::new);

        return new StubHelper<>(array);
    }

    public static StubHelper<PermissionDto> createPermission(int number) {
        var faker = new Faker();
        var array = IntStream.range(0, number)
                .boxed()
                .map(i -> new PermissionDto().setName(faker.hacker().verb()))
                .toArray(PermissionDto[]::new);

        return new StubHelper<>(array);
    }

    public static StubHelper<RoleDto> createRole(int number, int randomPermissionNumber) {
        var faker = new Faker();
        var array = IntStream.range(0, number)
                .boxed()
                .map(i -> new RoleDto()
                        .setName(faker.job().title())
                        .setPermissions(createPermission(chooseRandom(randomPermissionNumber)).asList()))
                .toArray(RoleDto[]::new);

        return new StubHelper<>(array);
    }

    public static StubHelper<UserDto> createUser(int number, int randomRoleNumber, int randomPermissionNumber) {
        var faker = new Faker();
        var array = IntStream.range(0, number)
                .boxed()
                .map(i -> new UserDto()
                        .setUsername(faker.name().fullName())
                        .setPassword(faker.internet().password())
                        .setEmail(faker.internet().emailAddress())
                        .setEnabled(true)
                        .setRoles(createRole(chooseRandom(randomRoleNumber), randomPermissionNumber).asList()))
                .toArray(UserDto[]::new);

        return new StubHelper<>(array);
    }
}