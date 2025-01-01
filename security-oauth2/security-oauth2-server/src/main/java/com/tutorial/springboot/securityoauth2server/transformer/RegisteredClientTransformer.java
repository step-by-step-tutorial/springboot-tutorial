package com.tutorial.springboot.securityoauth2server.transformer;

import com.tutorial.springboot.securityoauth2server.entity.Client;
import com.tutorial.springboot.securityoauth2server.entity.GrantType;
import com.tutorial.springboot.securityoauth2server.entity.Scope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Component
public class RegisteredClientTransformer {

    @Autowired
    private GrantTypeTransformer grantTypeTransformer;

    @Autowired
    private ScopeTransformer scopeTransformer;

    public RegisteredClient toRegisteredClient(Client client) {
        var registeredClientBuilder = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(client.getClientId())
                .clientSecret(client.getClientSecret())
                .redirectUri(client.getRedirectUri());

        client.getGrantTypes().forEach(it -> registeredClientBuilder.authorizationGrantType(new AuthorizationGrantType(it.getName())));
        client.getScopes().forEach(it -> registeredClientBuilder.scope(it.getName()));

        return registeredClientBuilder.build();
    }

    public Client toClient(RegisteredClient registeredClient) {
        var authorizationGrantTypeValues = registeredClient.getAuthorizationGrantTypes()
                .stream()
                .map(AuthorizationGrantType::getValue)
                .collect(toList());

        return new Client()
                .setClientId(registeredClient.getClientId())
                .setClientSecret(registeredClient.getClientSecret())
                .setGrantTypes(grantTypeTransformer.fromStringList(authorizationGrantTypeValues))
                .setRedirectUri(registeredClient.getRedirectUris().stream().findFirst().orElseThrow())
                .setScopes(scopeTransformer.fromStringList(new ArrayList<>(registeredClient.getScopes())));
    }

}
