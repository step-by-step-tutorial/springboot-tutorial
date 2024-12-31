package com.tutorial.springboot.securityoauth2server.transformer;

import com.tutorial.springboot.securityoauth2server.entity.Client;
import com.tutorial.springboot.securityoauth2server.entity.Scope;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public final class RegisteredClientTransformer {

    private RegisteredClientTransformer() {
    }

    public static RegisteredClient toRegisteredClient(Client client) {
        return RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(client.getClientId())
                .clientSecret(client.getClientSecret())
                .authorizationGrantTypes(authorizationGrantTypes ->
                        authorizationGrantTypes.addAll(convertStringListToAuthorizationGrantTypes(client.getGrantTypes()))
                )
                .redirectUri(client.getRedirectUri())
                .scope("read")
                .build();
    }

    public static Client toClient(RegisteredClient client) {
        return new Client()
                .setClientId(client.getClientId())
                .setClientSecret(client.getClientSecret())
                .setGrantTypes(
                        client.getAuthorizationGrantTypes().stream().map(AuthorizationGrantType::getValue).toList()
                )
                .setRedirectUri(client.getRedirectUris().stream().findFirst().orElseThrow())
                .setScopes(client.getScopes().stream().map(s -> new Scope().setName(s)).collect(Collectors.toList()));
    }


    public static List<AuthorizationGrantType> convertStringListToAuthorizationGrantTypes(List<String> grantTypeStrings) {
        return grantTypeStrings.stream()
                .map(it -> new AuthorizationGrantType(it.toLowerCase()))
                .collect(Collectors.toList());
    }
}
