package com.tutorial.springboot.security_authentication_inmemory.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UserDto {

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("roles")
    private List<String> roles = List.of();

    public String username() {
        return username;
    }

    public UserDto username(String username) {
        this.username = username;
        return this;
    }

    public String password() {
        return password;
    }

    public UserDto password(String password) {
        this.password = password;
        return this;
    }

    public List<String> roles() {
        return roles;
    }

    public UserDto roles(List<String> roles) {
        this.roles = roles;
        return this;
    }
}
