package com.tutorial.springboot.rbac.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> authorities = new ArrayList<>();

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

    @Override
    public List<Role> getAuthorities() {
        return authorities;
    }

    @Override
    public void updateFrom(User newOne) {
        super.updateFrom(newOne);
        this.username = newOne.username;
        this.password = newOne.password;
        this.email = newOne.email;
        this.enabled = newOne.enabled;
    }

    @Transient
    public List<String> getPermission() {
        return getAuthorities()
                .stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::getName)
                .toList();
    }
}
