package com.tutorial.springboot.securityoauth2client.transformer;

import com.tutorial.springboot.securityoauth2client.entity.Client;
import com.tutorial.springboot.securityoauth2client.entity.Scope;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

public final class ClientRegistrationTransformer {

    private ClientRegistrationTransformer() {
    }

    public static ClientRegistration toClientRegistration(Client entity) {
        return ClientRegistration.withRegistrationId(entity.getRegistrationId())
                .clientId(entity.getClientId())
                .clientName(entity.getClientName())
                .clientSecret(entity.getClientSecret())
                .scope(entity.getScopes().stream().map(Scope::getName).toList())
                .authorizationUri(entity.getAuthorizationUri())
                .tokenUri(entity.getTokenUri())
                .redirectUri(entity.getRedirectUri())
                .userInfoUri(entity.getUserInfoUri())
                .userNameAttributeName(entity.getUserNameAttributeName())
                .authorizationGrantType(new AuthorizationGrantType(entity.getAuthorizationGrantType()))
                .build();
    }

    public static Client toClientEntity(ClientRegistration clientRegistration) {
        return new Client()
                .setClientName(clientRegistration.getClientName())
                .setRegistrationId(clientRegistration.getRegistrationId())
                .setClientId(clientRegistration.getClientId())
                .setClientSecret(clientRegistration.getClientSecret())
                .setAuthorizationUri(clientRegistration.getProviderDetails().getAuthorizationUri())
                .setScopes(clientRegistration.getScopes().stream().map(s -> new Scope().setName(s)).toList())
                .setAuthorizationGrantType(clientRegistration.getAuthorizationGrantType().getValue())
                .setTokenUri(clientRegistration.getProviderDetails().getTokenUri())
                .setUserNameAttributeName(clientRegistration.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName())
                .setUserInfoUri(clientRegistration.getProviderDetails().getUserInfoEndpoint().getUri())
                .setRedirectUri(clientRegistration.getRedirectUri());
    }
}
