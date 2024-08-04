package com.tutorial.springboot.abac;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class PasswordGeneratorTest {

    @Test
    void contextLoads() {
        var encoder = new BCryptPasswordEncoder();
        System.out.println("encoder.encode(\"admin\") = " + encoder.encode("admin"));
        System.out.println("encoder.encode(\"user\") = " + encoder.encode("user"));
    }

}
