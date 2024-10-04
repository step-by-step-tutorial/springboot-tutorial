package com.tutorial.springboot.securityoauth2server.dto;

import java.util.List;

public class ClientDto extends AbstractDto<Long, ClientDto> {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private List<String> grantTypes;
    private List<String> scopes;
    private Integer accessTokenValiditySeconds;
    private Integer refreshTokenValiditySeconds;

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

    public String getRedirectUri() {
        return redirectUri;
    }

    public ClientDto setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
        return this;
    }

    public List<String> getGrantTypes() {
        return grantTypes;
    }

    public ClientDto setGrantTypes(List<String> grantTypes) {
        this.grantTypes = grantTypes;
        return this;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public ClientDto setScopes(List<String> scopes) {
        this.scopes = scopes;
        return this;
    }

    public Integer getAccessTokenValiditySeconds() {
        return accessTokenValiditySeconds;
    }

    public ClientDto setAccessTokenValiditySeconds(Integer accessTokenValiditySeconds) {
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
        return this;
    }

    public Integer getRefreshTokenValiditySeconds() {
        return refreshTokenValiditySeconds;
    }

    public ClientDto setRefreshTokenValiditySeconds(Integer refreshTokenValiditySeconds) {
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
        return this;
    }
}
