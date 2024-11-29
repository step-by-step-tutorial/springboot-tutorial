package com.tutorial.springboot.securityoauth2client.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ClientDto extends AbstractDto<Long, ClientDto> {

    private String registrationId;

    private String clientId;

    private String clientSecret;

    private String clientAuthenticationMethod;

    private String authorizationGrantType;

    private String redirectUri;

    private String clientName;

    private String authorizationUri;

    private String tokenUri;

    private String jwkSetUri;

    private String issuerUri;

    private Map<String, Object> configurationMetadata = Collections.emptyMap();

    private String userInfoUri;

    private String authenticationMethod = "header";

    private String userNameAttributeName;

    private List<String> scopes = new ArrayList<>();


    public String getRegistrationId() {
        return registrationId;
    }

    public ClientDto setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
        return this;
    }

    public String getClientId() {
        return clientId;
    }

    public ClientDto setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public ClientDto setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    public String getClientAuthenticationMethod() {
        return clientAuthenticationMethod;
    }

    public ClientDto setClientAuthenticationMethod(String clientAuthenticationMethod) {
        this.clientAuthenticationMethod = clientAuthenticationMethod;
        return this;
    }

    public String getAuthorizationGrantType() {
        return authorizationGrantType;
    }

    public ClientDto setAuthorizationGrantType(String authorizationGrantType) {
        this.authorizationGrantType = authorizationGrantType;
        return this;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public ClientDto setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
        return this;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public ClientDto setScopes(List<String> scopes) {
        this.scopes = scopes;
        return this;
    }

    public String getClientName() {
        return clientName;
    }

    public ClientDto setClientName(String clientName) {
        this.clientName = clientName;
        return this;
    }


    public String getAuthorizationUri() {
        return authorizationUri;
    }

    public ClientDto setAuthorizationUri(String authorizationUri) {
        this.authorizationUri = authorizationUri;
        return this;
    }

    public String getTokenUri() {
        return tokenUri;
    }

    public ClientDto setTokenUri(String tokenUri) {
        this.tokenUri = tokenUri;
        return this;
    }

    public String getJwkSetUri() {
        return jwkSetUri;
    }

    public ClientDto setJwkSetUri(String jwkSetUri) {
        this.jwkSetUri = jwkSetUri;
        return this;
    }

    public String getIssuerUri() {
        return issuerUri;
    }

    public ClientDto setIssuerUri(String issuerUri) {
        this.issuerUri = issuerUri;
        return this;
    }

    public Map<String, Object> getConfigurationMetadata() {
        return configurationMetadata;
    }

    public ClientDto setConfigurationMetadata(Map<String, Object> configurationMetadata) {
        this.configurationMetadata = configurationMetadata;
        return this;
    }

    public String getUserInfoUri() {
        return userInfoUri;
    }

    public ClientDto setUserInfoUri(String userInfoUri) {
        this.userInfoUri = userInfoUri;
        return this;
    }

    public String getAuthenticationMethod() {
        return authenticationMethod;
    }

    public ClientDto setAuthenticationMethod(String authenticationMethod) {
        this.authenticationMethod = authenticationMethod;
        return this;
    }

    public String getUserNameAttributeName() {
        return userNameAttributeName;
    }

    public ClientDto setUserNameAttributeName(String userNameAttributeName) {
        this.userNameAttributeName = userNameAttributeName;
        return this;
    }
}
