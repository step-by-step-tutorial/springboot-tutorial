package com.tutorial.springboot.rbac.dto;

import java.util.ArrayList;
import java.util.List;

public class UserDto extends AbstractDto<Long, UserDto> {

    private String username;

    private String password;

    private String email;

    private boolean enabled = true;

    private List<RoleDto> roles = new ArrayList<>();

    public String getUsername() {
        return username;
    }

    public UserDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserDto setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public UserDto setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public List<RoleDto> getRoles() {
        return roles;
    }

    public UserDto setRoles(List<RoleDto> roles) {
        this.roles = roles;
        return this;
    }
}
