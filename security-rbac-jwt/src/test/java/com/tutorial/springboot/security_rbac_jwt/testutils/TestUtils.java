package com.tutorial.springboot.security_rbac_jwt.testutils;

import java.util.Random;

public final class TestUtils {

    private TestUtils() {
    }
    public static String generateString(int length) {
        if (length < 0) {
            throw new IllegalArgumentException("Length cannot be negative");
        }

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "abcdefghijklmnopqrstuvwxyz"
                + "0123456789";
        Random random = new Random();

        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            result.append(characters.charAt(index));
        }

        return result.toString();
    }
}