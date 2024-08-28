package com.tutorial.springboot.rbac.poc;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static com.tutorial.springboot.rbac.test_utils.TestUtils.chooseRandom;

@ActiveProfiles({"test"})
class TechnicalTest {

    @Test
    void generatePassword() {
        var encoder = new BCryptPasswordEncoder();
        System.out.println("encoder.encode(\"admin\") = " + encoder.encode("admin"));
        System.out.println("encoder.encode(\"user\") = " + encoder.encode("user"));
    }

    @Test
    void generateBase64() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
        keyGen.init(256);
        SecretKey secretKey = keyGen.generateKey();
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());

        System.out.println("Secret Key: " + encodedKey);
    }

    @Test
    void generateRandomNumber() {
        System.out.println("random = " + chooseRandom(1));
    }
}
