package com.tutorial.springboot.securityoauth2client.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.stereotype.Component;

@Component
public class ClientRegistrationFactory {

    @Value("${local.oauth2.authorizationserver}")
    private String authorizationserver;

    @Value("${local.oauth2.resourceserver}")
    private String resourceserver;

    @Value("${local.oauth2.clientapp}")
    private String clientapp;

    public ClientRegistration createTestClient() {
        return ClientRegistration.withRegistrationId("testClient")
                .clientName("Test Client")
                .clientId("testClient")
                .clientSecret("password")
                .authorizationUri(String.format("%s/oauth2/authorize", authorizationserver))
                .scope("read")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .tokenUri(String.format("%s/oauth2/token", resourceserver))
                .userNameAttributeName("sub")
                .userInfoUri(String.format("%s/api/v1/users/userinfo", resourceserver))
                .redirectUri(String.format("%s/login/oauth2/code/testClient", clientapp))
                .build();
    }

    public ClientRegistration createAdminClient() {
        return ClientRegistration.withRegistrationId("adminClient")
                .clientName("Admin Client")
                .clientId("adminClient")
                .clientSecret("password")
                .authorizationUri(String.format("%s/oauth2/authorize", authorizationserver))
                .scope("read")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .tokenUri(String.format("%s/oauth2/token", resourceserver))
                .userNameAttributeName("sub")
                .userInfoUri(String.format("%s/api/v1/users/userinfo", resourceserver))
                .redirectUri(String.format("%s/login/oauth2/code/adminClient", clientapp))
                .build();
    }
}
