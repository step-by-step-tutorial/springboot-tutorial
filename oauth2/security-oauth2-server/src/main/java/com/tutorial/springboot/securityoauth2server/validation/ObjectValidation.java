package com.tutorial.springboot.securityoauth2server.validation;

import java.util.Collection;
import java.util.Objects;

public final class ObjectValidation {

    private ObjectValidation() {
    }

    public static void shouldBeNotNullOrEmpty(Collection<?> collection, String errorMessage) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static void shouldBeNotNullOrEmpty(String str, String errorMessage) {
        if (Objects.isNull(str) || str.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static boolean isNotNullOrEmpty(String str) {
        return (Objects.isNull(str) || str.trim().isEmpty());
    }

}
