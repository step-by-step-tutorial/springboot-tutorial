package com.tutorial.springboot.securityoauth2client.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Client extends AbstractEntity<Long, Client> {

    @Column(unique = true, nullable = false)
    private String clientId;

    @Column(nullable = false)
    private String clientSecret;

    private String redirectUri;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "client_grant_types", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "grant_type")
    private List<String> grantTypes = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "client_scopes",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "scope_id")
    )
    private List<Scope> scopes = new ArrayList<>();

    private Integer accessTokenValiditySeconds;

    private Integer refreshTokenValiditySeconds;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private List<AccessToken> accessTokens = new ArrayList<>();

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

    public String getRedirectUri() {
        return redirectUri;
    }

    public Client setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
        return this;
    }

    public List<String> getGrantTypes() {
        return grantTypes;
    }

    public Client setGrantTypes(List<String> grantTypes) {
        this.grantTypes = grantTypes;
        return this;
    }

    public List<Scope> getScopes() {
        return scopes;
    }

    public Client setScopes(List<Scope> scopes) {
        this.scopes = scopes;
        return this;
    }

    public Integer getAccessTokenValiditySeconds() {
        return accessTokenValiditySeconds;
    }

    public Client setAccessTokenValiditySeconds(Integer accessTokenValiditySeconds) {
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
        return this;
    }

    public Integer getRefreshTokenValiditySeconds() {
        return refreshTokenValiditySeconds;
    }

    public Client setRefreshTokenValiditySeconds(Integer refreshTokenValiditySeconds) {
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
        return this;
    }

    public List<AccessToken> getAccessTokens() {
        return accessTokens;
    }

    public void setAccessTokens(List<AccessToken> accessTokens) {
        this.accessTokens = accessTokens;
    }
}
