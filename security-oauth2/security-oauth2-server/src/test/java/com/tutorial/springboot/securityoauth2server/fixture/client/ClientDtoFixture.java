package com.tutorial.springboot.securityoauth2server.fixture.client;

import com.tutorial.springboot.securityoauth2server.dto.ClientDto;
import com.tutorial.springboot.securityoauth2server.enums.GrantType;
import net.datafaker.Faker;

import java.util.Arrays;

public final class ClientDtoFixture {

    private static final Faker faker = new Faker();

    private ClientDtoFixture() {
    }

    public static ClientDto newGivenClient() {
        var name = faker.app().name().replace(" ", "_");
        return new ClientDto()
                .setClientId(name)
                .setClientSecret("password")
                .setRedirectUri("http://localhost:8080/login/oauth2/code/" + name)
                .setGrantTypes(GrantType.allType())
                .setScopes(Arrays.asList("read", "write"))
                .setAccessTokenValiditySeconds(3600)
                .setRefreshTokenValiditySeconds(1209600);
    }

    public static ClientDto newGivenClient(String name) {
        return new ClientDto()
                .setClientId(name)
                .setClientSecret("password")
                .setRedirectUri("http://localhost:8080/login/oauth2/code/" + name)
                .setGrantTypes(GrantType.allType())
                .setScopes(Arrays.asList("read", "write"))
                .setAccessTokenValiditySeconds(3600)
                .setRefreshTokenValiditySeconds(1209600);
    }
}
