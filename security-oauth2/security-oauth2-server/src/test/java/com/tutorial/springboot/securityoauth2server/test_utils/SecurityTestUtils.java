package com.tutorial.springboot.securityoauth2server.test_utils;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

public final class SecurityTestUtils {

    public static final String TEST_USERNAME = "test";

    public static final String TEST_PASSWORD = "test";

    private SecurityTestUtils() {
    }

    public static void loginToTestEnv() {
        loginToTestEnv(TEST_USERNAME, TEST_PASSWORD);
    }

    public static void loginToTestEnv(String username, String password) {
        var auth = new UsernamePasswordAuthenticationToken(username, password);
        SecurityContextHolder.setContext(new SecurityContextImpl(auth));
    }
}
