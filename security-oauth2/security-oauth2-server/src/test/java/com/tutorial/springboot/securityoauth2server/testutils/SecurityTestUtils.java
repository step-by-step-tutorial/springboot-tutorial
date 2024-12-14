package com.tutorial.springboot.securityoauth2server.testutils;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import static com.tutorial.springboot.securityoauth2server.testutils.TestUtils.TEST_PASSWORD;
import static com.tutorial.springboot.securityoauth2server.testutils.TestUtils.TEST_USERNAME;

public final class SecurityTestUtils {

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
