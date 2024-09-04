//package com.tutorial.springboot.securityoauth2server.service.impl;
//
//import com.tutorial.springboot.securityoauth2server.entity.OAuthClient;
//import com.tutorial.springboot.securityoauth2server.entity.Scope;
//import com.tutorial.springboot.securityoauth2server.repository.OAuthClientRepository;
//import org.springframework.security.oauth2.core.AuthorizationGrantType;
//import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
//import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
//import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
//import org.springframework.stereotype.Service;
//
//import java.util.stream.Collectors;
//
//@Service
//public class RegisteredClientService implements RegisteredClientRepository {
//
//    private final OAuthClientRepository repository;
//
//    public RegisteredClientService(OAuthClientRepository repository) {
//        this.repository = repository;
//    }
//
//    @Override
//    public void save(RegisteredClient registeredClient) {
//        var entity = new OAuthClient();
//        entity.setClientId(registeredClient.getClientId());
//        entity.setClientSecret(registeredClient.getClientSecret());
//        entity.setRedirectUri(registeredClient.getRedirectUris().stream().findFirst().orElse(null));
//        entity.setGrantTypes(registeredClient.getAuthorizationGrantTypes().stream().map(AuthorizationGrantType::getValue).toList());
//        entity.setScopes(registeredClient.getScopes().stream().map(s -> new Scope().setName(s)).toList());
//        entity.setAccessTokenValiditySeconds((int) registeredClient.getTokenSettings().getAccessTokenTimeToLive().getSeconds());
//        entity.setRefreshTokenValiditySeconds((int) registeredClient.getTokenSettings().getRefreshTokenTimeToLive().getSeconds());
//
//        repository.save(entity);
//    }
//
//    @Override
//    public RegisteredClient findById(String id) {
//        var entity = repository.findByClientId(id).orElseThrow();
//        return convertToRegisteredClient(entity);
//    }
//
//    @Override
//    public RegisteredClient findByClientId(String clientId) {
//        var entity = repository.findByClientId(clientId).orElseThrow();
//        return convertToRegisteredClient(entity);
//    }
//
//    private RegisteredClient convertToRegisteredClient(OAuthClient entity) {
//        return RegisteredClient.withId(entity.getId().toString())
//                .clientId(entity.getClientId())
//                .clientSecret(entity.getClientSecret())
//                .scopes(scopes -> scopes.addAll(entity.getScopes().stream().map(scope -> scope.getName()).toList()))
//                .authorizationGrantTypes(grantTypes -> grantTypes.addAll(entity.getGrantTypes().stream().map(s -> new AuthorizationGrantType(s)).collect(Collectors.toSet())))
//                .redirectUris(uris -> uris.add(entity.getRedirectUri()))
//                .tokenSettings(TokenSettings.builder()
//                        .accessTokenTimeToLive(java.time.Duration.ofSeconds(entity.getAccessTokenValiditySeconds()))
//                        .refreshTokenTimeToLive(java.time.Duration.ofSeconds(entity.getRefreshTokenValiditySeconds()))
//                        .build())
//                .build();
//    }
//}
