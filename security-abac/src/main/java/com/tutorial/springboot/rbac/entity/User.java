package com.tutorial.springboot.rbac.entity;

import jakarta.persistence.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;

@Entity
@Table(name = "users")
public class User extends AbstractEntity<Long, User> implements UserDetails {

    private String username;

    private String password;

    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> authorities;

    @ElementCollection
    private Map<String, String> attributes;

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
    public List<Role> getAuthorities() {
        return authorities;
    }

    public User setAuthorities(List<Role> roles) {
        this.authorities = roles;
        return this;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public User setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
        return this;
    }

    @Transient
    public List<String> getPermission() {
        return getAuthorities()
                .stream()
                .map(Role::getPermissions)
                .flatMap(List::stream)
                .map(Permission::getName)
                .toList();
    }
}
