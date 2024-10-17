package com.tutorial.springboot.securityoauth2client.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User extends AbstractEntity<Long, User> implements UserDetails {

    @NotBlank(message = "Username is mandatory")
    private String username;

    @NotBlank(message = "Password is mandatory")
    private String password;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email is not valid")
    private String email;

    private boolean enabled = true;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<AccessToken> accessTokens = new ArrayList<>();

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

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
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

    public List<Role> getRoles() {
        return roles;
    }

    public User setRoles(List<Role> roles) {
        this.roles = roles;
        return this;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    public List<AccessToken> getAccessTokens() {
        return accessTokens;
    }

    public void setAccessTokens(List<AccessToken> accessTokens) {
        this.accessTokens = accessTokens;
    }

    @Override
    public void updateFrom(User newOne) {
        super.updateFrom(newOne);
        this.username = newOne.username;
        this.password = newOne.password;
        this.email = newOne.email;
        this.enabled = newOne.enabled;
        this.roles.clear();
        this.roles.addAll(newOne.roles);
    }

    @Transient
    public List<String> getPermissions() {
        return getRoles()
                .stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::getName)
                .toList();
    }
}
