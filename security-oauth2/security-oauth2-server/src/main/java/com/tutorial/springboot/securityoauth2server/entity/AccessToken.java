package com.tutorial.springboot.securityoauth2server.entity;

import jakarta.persistence.*;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class AccessToken extends AbstractEntity<Long, AccessToken> {

    @NotBlank(message = "Token is mandatory")
    @Column(nullable = false)
    @Lob
    private byte[] token;

    private LocalDateTime expiration;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "accessToken")
    private RefreshToken refreshToken;

    @Override
    public AccessToken updateFrom(AccessToken newOne) {
        super.updateFrom(newOne);
        this.token = newOne.token;
        this.expiration = newOne.expiration;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        var that = (AccessToken) o;
        return Objects.equals(user.getUsername(), that.getUser().getUsername()) &&
               Objects.equals(client.getClientId(), that.getClient().getClientId()) &&
               Objects.equals(getExpiration(), that.getExpiration());
    }

    @Override
    public int hashCode() {
        return Objects.hash(user.getUsername(), client.getClientId(), getExpiration());
    }

    @Override
    public String toString() {
        var template = """
                AccessToken: {user:%, client:%, expiration:%}
                """;
        return template.formatted(user.getUsername(), client.getClientId(), expiration);
    }

    public byte[] getToken() {
        return token;
    }

    public AccessToken setToken(byte[] token) {
        this.token = token;
        return this;
    }

    public Client getClient() {
        return client;
    }

    public AccessToken setClient(Client client) {
        this.client = client;
        return this;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    public AccessToken setExpiration(LocalDateTime expiration) {
        this.expiration = expiration;
        return this;
    }

    public User getUser() {
        return user;
    }

    public AccessToken setUser(User user) {
        this.user = user;
        return this;
    }

    public RefreshToken getRefreshToken() {
        return refreshToken;
    }

    public AccessToken setRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }
}
