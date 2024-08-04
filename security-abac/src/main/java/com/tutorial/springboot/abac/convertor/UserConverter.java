package com.tutorial.springboot.abac.convertor;

import com.tutorial.springboot.abac.dto.UserDto;
import com.tutorial.springboot.abac.model.User;

import java.util.stream.Collectors;

public final class UserConverter {

    private UserConverter() {
        // Private constructor to prevent instantiation
    }

    public static UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getPassword(), user.isEnabled(),
                user.getAuthorities().stream().map(RoleConverter::toDto).collect(Collectors.toSet()));
    }

    public static User toEntity(UserDto userDto) {
        User user = new User();
        user.setId(userDto.id());
        user.setUsername(userDto.username());
        user.setPassword(userDto.password());
        user.setEnabled(userDto.enabled());
        user.setAuthorities(userDto.roles().stream().map(RoleConverter::toEntity).collect(Collectors.toSet()));
        return user;
    }
}
