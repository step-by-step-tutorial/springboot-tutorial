package com.tutorial.springboot.rbac.test_utils;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import java.util.Random;

public final class TestUtils {

    private TestUtils() {
    }

    public static int chooseRandom(int bound) {
        if (bound <= 0) {
            return 0;
        }

        var random = new Random();
        return random.nextInt(1, 1 + bound);
    }

    public static void loginByAdmin(){
        loginByUser("admin", "admin");
    }

    public static void loginByUser(String username, String password){
        var auth = new UsernamePasswordAuthenticationToken(username, password);
        SecurityContextHolder.setContext(new SecurityContextImpl(auth));
    }
}
