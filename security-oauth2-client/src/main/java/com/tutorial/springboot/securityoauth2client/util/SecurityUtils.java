package com.tutorial.springboot.securityoauth2client.util;

import com.tutorial.springboot.securityoauth2client.dto.OAuth2AuthenticationDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static String getCurrentUsername() {
        try {
            var authenticationToken = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            var user = authenticationToken.getPrincipal();
            return user.getName();
        } catch (Exception e) {
            return "no user";
        }
    }

    public static OAuth2AuthenticationDto getCurrentOAuth2Authentication() {
        var authenticationToken = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        var user = authenticationToken.getPrincipal();
        return new OAuth2AuthenticationDto(
                user.getName(),
                authenticationToken.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList(),
                user.getAttributes(),
                authenticationToken.isAuthenticated(),
                authenticationToken.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList()
        );
    }
}
