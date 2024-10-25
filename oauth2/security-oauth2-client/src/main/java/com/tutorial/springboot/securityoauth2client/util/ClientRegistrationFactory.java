package com.tutorial.springboot.securityoauth2client.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.stereotype.Component;

@Component
public class ClientRegistrationFactory {

    @Value( "${local.oauth2.server-url}")
    private String serverUrl;

    @Value( "${local.oauth2.app-url}")
    private String clientUrl;

    public ClientRegistration createTestClient() {
        return ClientRegistration.withRegistrationId("testClient")
                .clientName("Test Client")
                .clientId("testClient")
                .clientSecret("password")
                .authorizationUri(String.format("%s/oauth2/authorize", serverUrl))
                .scope("read")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .tokenUri(String.format("%s/oauth2/token", serverUrl))
                .userNameAttributeName("sub")
                .userInfoUri(String.format("%s/api/v1/users/userinfo", serverUrl))
                .redirectUri(String.format("%s/login/oauth2/code/testClient", clientUrl))
                .build();
    }

    public ClientRegistration createAdminClient() {
        return ClientRegistration.withRegistrationId("adminClient")
                .clientName("Admin Client")
                .clientId("adminClient")
                .clientSecret("password")
                .authorizationUri(String.format("%s/oauth2/authorize", serverUrl))
                .scope("read")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .tokenUri(String.format("%s/oauth2/token", serverUrl))
                .userNameAttributeName("sub")
                .userInfoUri(String.format("%s/api/v1/users/userinfo", serverUrl))
                .redirectUri(String.format("%s/login/oauth2/code/adminClient", clientUrl))
                .build();
    }
}
