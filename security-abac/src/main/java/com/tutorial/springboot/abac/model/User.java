package com.tutorial.springboot.abac.model;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.AbstractAuditable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends AbstractAuditable<User, Long> implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> authorities;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Transient
    public User addId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public User setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public Collection<Role> getAuthorities() {
        return authorities;
    }

    public User setAuthorities(Set<Role> roles) {
        this.authorities = roles;
        return this;
    }

    @Transient
    public List<String> getPermission() {
        return getAuthorities()
                .stream()
                .map(Role::getPermissions)
                .flatMap(Set::stream)
                .map(Permission::getName)
                .toList();
    }
}
