package com.tutorial.springboot.securityoauth2server.entity;

import com.tutorial.springboot.securityoauth2server.util.CollectionUtils;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Client extends AbstractEntity<Long, Client> {

    @Column(unique = true, nullable = false)
    private String clientId;

    @Column(nullable = false)
    private String clientSecret;

    private String redirectUri;

    private Integer accessTokenValiditySeconds;

    private Integer refreshTokenValiditySeconds;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "client_grant_types", joinColumns = @JoinColumn(name = "client_id"))
    @Column(name = "grant_type")
    private List<String> grantTypes = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "client_scopes",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "scope_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"client_id", "scope_id"})
    )
    private List<Scope> scopes = new ArrayList<>();

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

    public Client setAccessTokens(List<AccessToken> accessTokens) {
        this.accessTokens = accessTokens;
        return this;
    }

    @Override
    public Client updateFrom(Client newOne) {
        super.updateFrom(newOne);
        this.clientId = newOne.clientId;
        this.clientSecret = newOne.clientSecret;
        this.redirectUri = newOne.redirectUri;
        this.grantTypes = newOne.grantTypes;
        this.scopes = newOne.scopes;
        this.accessTokenValiditySeconds = newOne.accessTokenValiditySeconds;
        this.refreshTokenValiditySeconds = newOne.refreshTokenValiditySeconds;
        this.accessTokens = newOne.accessTokens;
        return this;
    }

    @Override
    public Client updateRelations(Client newOne) {
        var compared = CollectionUtils.compareCollections(this.scopes, newOne.scopes);

        this.scopes.removeAll(compared.deletionItems());
        this.scopes.addAll(compared.newItems());

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(clientId, client.clientId) && Objects.equals(clientSecret, client.clientSecret) && Objects.equals(redirectUri, client.redirectUri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, clientSecret, redirectUri);
    }

    @Override
    public String toString() {
        return clientId;
    }
}
