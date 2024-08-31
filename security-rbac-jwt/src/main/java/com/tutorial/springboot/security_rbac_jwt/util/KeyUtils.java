package com.tutorial.springboot.security_rbac_jwt.util;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public final class KeyUtils {

    private KeyUtils() {
    }

    public static String generateSecretKey() {
        try {
            var keyGen = KeyGenerator.getInstance("HmacSHA256");
            keyGen.init(256);
            var secretKey = keyGen.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static SecretKey getSignInKey(String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
