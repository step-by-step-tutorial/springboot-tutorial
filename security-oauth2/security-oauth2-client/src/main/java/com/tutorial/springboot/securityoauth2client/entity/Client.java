package com.tutorial.springboot.securityoauth2client.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Client extends AbstractEntity<Long, Client> {

    private String registrationId;

    @Column(unique = true, nullable = false)
    private String clientId;

    private String clientSecret;

    private String clientAuthenticationMethod;

    private String authorizationGrantType;

    private String redirectUri;

    @Column(unique = true, nullable = false)
    private String clientName;

    private String authorizationUri;

    private String tokenUri;

    private String jwkSetUri;

    private String issuerUri;

    @Convert(converter = JsonAttributeConverter.class)
    private Map<String, Object> configurationMetadata = new HashMap<>();

    private String userInfoUri;

    private String authenticationMethod = "header";

    private String userNameAttributeName;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "client_scopes",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "scope_id")
    )
    private List<Scope> scopes = new ArrayList<>();

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

    public String getUserInfoUri() {
        return userInfoUri;
    }

    public Client setUserInfoUri(String uri) {
        this.userInfoUri = uri;
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

    public List<Scope> getScopes() {
        return scopes;
    }

    public Client setScopes(List<Scope> scopes) {
        this.scopes = scopes;
        return this;
    }
}
