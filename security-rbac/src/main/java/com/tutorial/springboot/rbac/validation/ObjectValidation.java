package com.tutorial.springboot.rbac.validation;

import java.util.Collection;
import java.util.Objects;

public final class ObjectValidation {

    private ObjectValidation() {
    }

    public static void requireNonEmpty(Collection<?> collection, String errorMessage) {
        if (collection.isEmpty()) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public static void requireNonEmpty(String str, String errorMessage) {
        if (Objects.isNull(str) || str.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

}
