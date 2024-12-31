package com.tutorial.springboot.securityoauth2server.proofofconcept;

import com.tutorial.springboot.securityoauth2server.fixture.client.ClientDtoFixture;
import com.tutorial.springboot.securityoauth2server.service.impl.TokenService;
import com.tutorial.springboot.securityoauth2server.testutils.TestAuthenticationHelper;
import com.tutorial.springboot.securityoauth2server.util.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import javax.crypto.KeyGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@SpringBootTest
@ActiveProfiles({"test", "h2"})
class ProofOfConceptTest {

    @Autowired
    private TokenService tokenService;

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

        System.out.println("Secret Key = " + encodedKey);
    }

    @Test
    void generateTestToken() {
        TestAuthenticationHelper.login();
        var tokenDto = tokenService.generateToken(SecurityUtils.getCurrentUsername(), ClientDtoFixture.newGivenClient());
        System.out.println("token = " + tokenDto.orElseThrow().token());
        System.out.println("token expiration = " + tokenDto.orElseThrow().expiration());
    }
}
