package com.tutorial.springboot.securityoauth2server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class AuthorizationCode extends AbstractEntity<Long, AuthorizationCode> {

    @NotBlank(message = "Code is mandatory")
    @Column(nullable = false)
    private String code;

    private LocalDateTime expiration;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public AuthorizationCode updateFrom(AuthorizationCode newOne) {
        super.updateFrom(newOne);
        this.code = newOne.code;
        this.expiration = newOne.expiration;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        var that = (AuthorizationCode) o;
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
                AuthorizationCode: {client:%, user:%, expiration:%}
                """;
        return template.formatted(client.getClientId(), user.getUsername(), expiration);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    public void setExpiration(LocalDateTime expiration) {
        this.expiration = expiration;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
