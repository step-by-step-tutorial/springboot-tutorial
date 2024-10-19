package com.tutorial.springboot.securityoauth2client.entity;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;

import java.util.HashMap;
import java.util.Map;

@Entity
public class Client extends AbstractEntity<Long, Client> {

    private String registrationId;

    private String clientId;

    private String clientSecret;

    private String clientAuthenticationMethod;

    private String authorizationGrantType;

    private String redirectUri;

    private String scope;

    private String clientName;

    private String authorizationUri;

    private String tokenUri;

    private String jwkSetUri;

    private String issuerUri;

    @Convert(converter = JsonAttributeConverter.class)
    private Map<String, Object> configurationMetadata = new HashMap<>();

    private String uri;

    private String authenticationMethod = "header";

    private String userNameAttributeName;

    public String getRegistrationId() {
        return registrationId;
    }

    public Client setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
        return this;
    }

    public String getClientId() {
        return clientId;
    }

    public Client setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public Client setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    public String getClientAuthenticationMethod() {
        return clientAuthenticationMethod;
    }

    public Client setClientAuthenticationMethod(String clientAuthenticationMethod) {
        this.clientAuthenticationMethod = clientAuthenticationMethod;
        return this;
    }

    public String getAuthorizationGrantType() {
        return authorizationGrantType;
    }

    public Client setAuthorizationGrantType(String authorizationGrantType) {
        this.authorizationGrantType = authorizationGrantType;
        return this;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public Client setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
        return this;
    }

    public String getScope() {
        return scope;
    }

    public Client setScope(String scope) {
        this.scope = scope;
        return this;
    }

    public String getClientName() {
        return clientName;
    }

    public Client setClientName(String clientName) {
        this.clientName = clientName;
        return this;
    }


    public String getAuthorizationUri() {
        return authorizationUri;
    }

    public Client setAuthorizationUri(String authorizationUri) {
        this.authorizationUri = authorizationUri;
        return this;
    }

    public String getTokenUri() {
        return tokenUri;
    }

    public Client setTokenUri(String tokenUri) {
        this.tokenUri = tokenUri;
        return this;
    }

    public String getJwkSetUri() {
        return jwkSetUri;
    }

    public Client setJwkSetUri(String jwkSetUri) {
        this.jwkSetUri = jwkSetUri;
        return this;
    }

    public String getIssuerUri() {
        return issuerUri;
    }

    public Client setIssuerUri(String issuerUri) {
        this.issuerUri = issuerUri;
        return this;
    }

    public Map<String, Object> getConfigurationMetadata() {
        return configurationMetadata;
    }

    public Client setConfigurationMetadata(Map<String, Object> configurationMetadata) {
        this.configurationMetadata = configurationMetadata;
        return this;
    }

    public String getUri() {
        return uri;
    }

    public Client setUri(String uri) {
        this.uri = uri;
        return this;
    }

    public String getAuthenticationMethod() {
        return authenticationMethod;
    }

    public Client setAuthenticationMethod(String authenticationMethod) {
        this.authenticationMethod = authenticationMethod;
        return this;
    }

    public String getUserNameAttributeName() {
        return userNameAttributeName;
    }

    public Client setUserNameAttributeName(String userNameAttributeName) {
        this.userNameAttributeName = userNameAttributeName;
        return this;
    }
}
