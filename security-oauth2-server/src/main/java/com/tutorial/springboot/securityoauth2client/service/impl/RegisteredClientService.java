package com.tutorial.springboot.securityoauth2client.service.impl;

import com.tutorial.springboot.securityoauth2client.repository.ClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RegisteredClientService implements RegisteredClientRepository {

    private final ClientRepository clientRepository;

    public RegisteredClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void save(RegisteredClient registeredClient) {

    }

    @Override
    public RegisteredClient findById(String id) {
        return clientRepository.findById(Long.parseLong(id))
                .map(client ->
                        RegisteredClient.withId(UUID.randomUUID().toString())
                                .clientId(client.getClientId())
                                .clientSecret(client.getClientSecret())
                                .authorizationGrantTypes(authorizationGrantTypes ->
                                        authorizationGrantTypes.addAll(convertStringListToAuthorizationGrantTypes(client.getGrantTypes()))
                                )
                                .redirectUri(client.getRedirectUri())
                                .scope("read")
                                .scope("write")
                                .build()
                ).orElseThrow();
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        return clientRepository.findByClientId(clientId)
                .map(client ->
                        RegisteredClient.withId(UUID.randomUUID().toString())
                                .clientId(client.getClientId())
                                .clientSecret(client.getClientSecret())
                                .authorizationGrantTypes(authorizationGrantTypes ->
                                        authorizationGrantTypes.addAll(convertStringListToAuthorizationGrantTypes(client.getGrantTypes()))
                                )
                                .redirectUri(client.getRedirectUri())
                                .scope("read")
                                .scope("write")
                                .build()
                ).orElseThrow();
    }

    public static List<AuthorizationGrantType> convertStringListToAuthorizationGrantTypes(List<String> grantTypeStrings) {
        List<AuthorizationGrantType> grantTypes = new ArrayList<>();

        for (String grantTypeString : grantTypeStrings) {
            AuthorizationGrantType grantType = new AuthorizationGrantType(grantTypeString.toLowerCase());
            grantTypes.add(grantType);
        }

        return grantTypes;
    }
}
