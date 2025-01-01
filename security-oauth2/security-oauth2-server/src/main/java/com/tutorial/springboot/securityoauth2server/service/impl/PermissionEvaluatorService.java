package com.tutorial.springboot.securityoauth2server.service.impl;

import com.tutorial.springboot.securityoauth2server.repository.rbac.UserRepository;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;

import static java.util.Objects.isNull;

@Component
public class PermissionEvaluatorService implements PermissionEvaluator {

    private final UserRepository userRepository;

    public PermissionEvaluatorService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (isNull(authentication) || !(authentication.getPrincipal() instanceof UserDetails)) {
            return false;
        }

        var username = String.valueOf(authentication.getPrincipal());
        return userRepository.findByUsername(username)
                .orElseThrow()
                .getPermissions()
                .contains(String.valueOf(permission));
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}
