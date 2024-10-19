package com.tutorial.springboot.securityoauth2client.util;

import com.tutorial.springboot.securityoauth2client.dto.ClientDto;

public final class ClientInitializerFactory {
    private ClientInitializerFactory() {
    }

    public static ClientDto createTestClient(String serverUrl, String clientUrl) {
        return new ClientDto()
                .setClientName("Test Client")
                .setRegistrationId("testClient")
                .setClientId("testClient")
                .setClientSecret("test-secret")
                .setAuthorizationUri(String.format("%s/oauth2/authorize", serverUrl))
                .setScope("read")
                .setAuthorizationGrantType("authorization_code")
                .setTokenUri(String.format("%s/oauth2/token", serverUrl))
                .setUserNameAttributeName("sub")
                .setUri(String.format("%s/api/v1/users/userinfo", serverUrl))
                .setRedirectUri(String.format("%s/login/oauth2/code/testClient", clientUrl));
    }
}
