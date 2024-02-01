package com.tutorial.springboot.rest_basic.util;

import java.util.Arrays;

public final class StringUtils {
    private static final String SPLIT_CHARACTER = ",";

    private StringUtils() {
    }

    public static Long[] stringToLongArray(String string) {
        try {
            return Arrays.stream(string.split(SPLIT_CHARACTER))
                    .map(Long::valueOf)
                    .toArray(Long[]::new);
        } catch (Exception ex) {
            throw new IllegalStateException("ID sequence contains non-numeric characters");
        }
    }
}
