package com.tutorial.springboot.securityoauth2server.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tutorial.springboot.securityoauth2server.dto.AbstractDto;
import com.tutorial.springboot.securityoauth2server.dto.PermissionDto;
import com.tutorial.springboot.securityoauth2server.dto.RoleDto;
import com.tutorial.springboot.securityoauth2server.validation.SaveValidation;
import com.tutorial.springboot.securityoauth2server.validation.UpdateValidation;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

public class UserDto extends AbstractDto<Long, UserDto> {

    @NotBlank(groups = {SaveValidation.class, UpdateValidation.class}, message = "username should not be blank")
    private String username;

    @NotBlank(groups = {SaveValidation.class, UpdateValidation.class}, message = "password should not be blank")
    private String password;

    @NotBlank(groups = {SaveValidation.class, UpdateValidation.class}, message = "email should not be blank")
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

    @JsonIgnore
    public List<String> getPermissions() {
        return roles.stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(PermissionDto::getName)
                .distinct()
                .toList();
    }
}
