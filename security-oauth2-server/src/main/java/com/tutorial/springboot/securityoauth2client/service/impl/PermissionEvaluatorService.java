package com.tutorial.springboot.securityoauth2client.service.impl;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Objects;

@Component
public class PermissionEvaluatorService implements PermissionEvaluator {

    private final UserService userService;

    public PermissionEvaluatorService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (Objects.isNull(authentication) || !(authentication.getPrincipal() instanceof UserDetails)) {
            return false;
        }

        return userService.findByUsername(String.valueOf(authentication.getPrincipal()))
                .getPermissions()
                .contains(String.valueOf(permission));
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}
