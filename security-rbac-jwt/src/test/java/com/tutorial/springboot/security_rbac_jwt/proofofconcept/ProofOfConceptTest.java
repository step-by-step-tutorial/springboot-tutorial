package com.tutorial.springboot.security_rbac_jwt.proofofconcept;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import javax.crypto.KeyGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@ActiveProfiles({"test"})
class ProofOfConceptTest {

    @Test
    void generatePassword() {
        var encoder = new BCryptPasswordEncoder();
        System.out.println("encoder.encode(\"admin\") = " + encoder.encode("admin"));
        System.out.println("encoder.encode(\"user\") = " + encoder.encode("user"));
        System.out.println("encoder.encode(\"test\") = " + encoder.encode("test"));
        System.out.println("encoder.encode(\"password\") = " + encoder.encode("password"));
    }

    @Test
    void generateBase64() throws NoSuchAlgorithmException {
        var keyGen = KeyGenerator.getInstance("HmacSHA256");
        keyGen.init(256);
        var secretKey = keyGen.generateKey();
        var encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());

        System.out.println("Secret Key: " + encodedKey);
    }

}
