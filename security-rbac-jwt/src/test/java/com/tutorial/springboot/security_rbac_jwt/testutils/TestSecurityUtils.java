package com.tutorial.springboot.security_rbac_jwt.testutils;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import static com.tutorial.springboot.security_rbac_jwt.testutils.TestHttpUtils.TEST_PASSWORD;
import static com.tutorial.springboot.security_rbac_jwt.testutils.TestHttpUtils.TEST_USERNAME;


public final class TestSecurityUtils {

    private TestSecurityUtils() {
    }

    public static void login() {
        login(TEST_USERNAME, TEST_PASSWORD);
    }

    public static void login(String username, String password) {
        var auth = new UsernamePasswordAuthenticationToken(username, password);
        SecurityContextHolder.setContext(new SecurityContextImpl(auth));
    }

}
