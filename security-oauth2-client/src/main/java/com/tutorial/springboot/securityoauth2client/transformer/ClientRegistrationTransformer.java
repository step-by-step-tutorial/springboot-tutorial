package com.tutorial.springboot.securityoauth2client.transformer;

import com.tutorial.springboot.securityoauth2client.entity.Client;
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
                .scope(entity.getScope())
                .authorizationUri(entity.getAuthorizationUri())
                .tokenUri(entity.getTokenUri())
                .redirectUri(entity.getRedirectUri())
                .userInfoUri(entity.getUri())
                .userNameAttributeName(entity.getUserNameAttributeName())
                .authorizationGrantType(new AuthorizationGrantType(entity.getAuthorizationGrantType()))
                .build();
    }
}
