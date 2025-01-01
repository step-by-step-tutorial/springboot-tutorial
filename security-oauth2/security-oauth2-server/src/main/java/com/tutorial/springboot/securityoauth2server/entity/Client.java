package com.tutorial.springboot.securityoauth2server.entity;

import com.tutorial.springboot.securityoauth2server.util.CollectionUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Audited
public class Client extends AbstractEntity<Long, Client> {

    @NotBlank(message = "Client ID is mandatory and unique")
    @Column(unique = true, nullable = false)
    private String clientId;

    @NotBlank(message = "Client secret is mandatory and unique")
    @Column(nullable = false)
    private String clientSecret;

    @NotBlank(message = "Redirect URI is mandatory and unique")
    @Column(nullable = false)
    private String redirectUri;

    @NotNull(message = "Access token validity seconds is mandatory")
    @Column(nullable = false)
    private Integer accessTokenValiditySeconds;

    @NotNull(message = "Refresh token validity seconds is mandatory")
    @Column(nullable = false)
    private Integer refreshTokenValiditySeconds;

    @NotAudited
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "client_grant_types",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "grant_type_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"client_id", "grant_type_id"})
    )
    private List<GrantType> grantTypes = new ArrayList<>();

    @NotAudited
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "client_scopes",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "scope_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"client_id", "scope_id"})
    )
    private List<Scope> scopes = new ArrayList<>();

    @NotAudited
    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private List<AccessToken> accessTokens = new ArrayList<>();

    @Override
    public Client updateFrom(Client newOne) {
        super.updateFrom(newOne);
        this.clientId = newOne.clientId;
        this.clientSecret = newOne.clientSecret;
        this.redirectUri = newOne.redirectUri;
        this.accessTokenValiditySeconds = newOne.accessTokenValiditySeconds;
        this.refreshTokenValiditySeconds = newOne.refreshTokenValiditySeconds;
        return this;
    }

    @Override
    public Client updateJoinTableRelations(Client newOne) {
        var scopesCompared = CollectionUtils.compareCollections(this.scopes, newOne.scopes);
        this.scopes.removeAll(scopesCompared.deletionItems());
        this.scopes.addAll(scopesCompared.newItems());

        var grantTypesCompared = CollectionUtils.compareCollections(this.grantTypes, newOne.grantTypes);
        this.grantTypes.removeAll(grantTypesCompared.deletionItems());
        this.grantTypes.addAll(grantTypesCompared.newItems());

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        var client = (Client) o;
        return Objects.equals(clientId, client.clientId) &&
               Objects.equals(clientSecret, client.clientSecret) &&
               Objects.equals(redirectUri, client.redirectUri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, clientSecret, redirectUri);
    }

    @Override
    public String toString() {
        return clientId;
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

    public String getRedirectUri() {
        return redirectUri;
    }

    public Client setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
        return this;
    }

    public List<GrantType> getGrantTypes() {
        return grantTypes;
    }

    public Client setGrantTypes(List<GrantType> grantTypes) {
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

}
