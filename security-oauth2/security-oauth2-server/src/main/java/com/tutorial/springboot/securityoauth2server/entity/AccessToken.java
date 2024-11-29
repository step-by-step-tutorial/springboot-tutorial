package com.tutorial.springboot.securityoauth2server.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class AccessToken extends AbstractEntity<Long, AccessToken> {

    @Column(nullable = false)
    @Lob
    private byte[] token;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime expiration;

    @OneToOne(mappedBy = "accessToken")
    private RefreshToken refreshToken;

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

    public User getUser() {
        return user;
    }

    public AccessToken setUser(User user) {
        this.user = user;
        return this;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    public AccessToken setExpiration(LocalDateTime expiration) {
        this.expiration = expiration;
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
