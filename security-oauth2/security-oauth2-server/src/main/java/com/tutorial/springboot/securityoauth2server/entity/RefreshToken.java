package com.tutorial.springboot.securityoauth2server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class RefreshToken extends AbstractEntity<Long, RefreshToken> {

    @NotBlank(message = "Token is mandatory")
    @Column(nullable = false)
    @Lob
    private byte[] token;

    private LocalDateTime expiration;

    @OneToOne
    @JoinColumn(name = "access_token_id")
    private AccessToken accessToken;

    @Override
    public RefreshToken updateFrom(RefreshToken newOne) {
        super.updateFrom(newOne);
        this.token = newOne.token;
        this.expiration = newOne.expiration;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        var that = (RefreshToken) o;
        return Objects.equals(accessToken.getUser().getUsername(), that.accessToken.getUser().getUsername()) &&
               Objects.equals(accessToken.getClient().getClientId(), that.accessToken.getClient().getClientId()) &&
               Objects.equals(getExpiration(), that.getExpiration());
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessToken.getUser().getUsername(), accessToken.getClient().getClientId(), getExpiration());
    }

    @Override
    public String toString() {
        var template = """
                RefreshToken: {user:%, client:%, expiration:%}
                """;
        return template.formatted(accessToken.getUser().getUsername(), accessToken.getClient().getClientId(), expiration);
    }
    public byte[] getToken() {
        return token;
    }

    public RefreshToken setToken(byte[] token) {
        this.token = token;
        return this;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    public RefreshToken setExpiration(LocalDateTime expiration) {
        this.expiration = expiration;
        return this;
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public RefreshToken setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
        return this;
    }
}
