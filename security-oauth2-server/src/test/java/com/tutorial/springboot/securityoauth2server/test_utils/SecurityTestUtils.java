package com.tutorial.springboot.securityoauth2server.test_utils;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

public final class SecurityTestUtils {

    private SecurityTestUtils() {
    }

    public static void authenticateToTestEnv() {
        authenticateToTestEnv("admin", "admin");
    }

    public static void authenticateToTestEnv(String username, String password) {
        var auth = new UsernamePasswordAuthenticationToken(username, password);
        SecurityContextHolder.setContext(new SecurityContextImpl(auth));
    }
}
