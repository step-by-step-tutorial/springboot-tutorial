package com.tutorial.springboot.rbac.poc;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.tutorial.springboot.rbac.test_utils.TestUtils.chooseRandom;

class TechnicalTest {

    @Test
    @Disabled
    void generatePassword() {
        var encoder = new BCryptPasswordEncoder();
        System.out.println("encoder.encode(\"admin\") = " + encoder.encode("admin"));
        System.out.println("encoder.encode(\"user\") = " + encoder.encode("user"));
    }

    @Test
    @Disabled
    void generateRandomNumber() {
        System.out.println("random = " + chooseRandom(1));
    }
}
