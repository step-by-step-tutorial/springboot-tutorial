package com.tutorial.springboot.security_rbac_jwt.service.impl;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;

import static java.util.Objects.isNull;

@Component
public class PermissionEvaluatorService implements PermissionEvaluator {

    private final UserService userService;

    public PermissionEvaluatorService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (isNull(authentication) || !(authentication.getPrincipal() instanceof UserDetails)) {
            return false;
        }

        var username = String.valueOf(authentication.getPrincipal());
        return userService.findByUsername(username)
                .getPermissions()
                .contains(String.valueOf(permission));
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}
