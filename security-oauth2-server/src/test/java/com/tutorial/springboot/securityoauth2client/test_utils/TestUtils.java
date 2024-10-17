package com.tutorial.springboot.securityoauth2client.test_utils;

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

}
