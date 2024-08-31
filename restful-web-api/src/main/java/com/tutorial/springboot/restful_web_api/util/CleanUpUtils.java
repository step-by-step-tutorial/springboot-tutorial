package com.tutorial.springboot.restful_web_api.util;

import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.Objects;
import java.util.stream.Stream;

public final class CleanUpUtils {

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private CleanUpUtils() {
    }

    public static <T> Stream<T> clean(T[] items) {
        return Stream.of(items).filter(Objects::nonNull)
                .filter((T object) -> validator.validate(object).isEmpty())
                .distinct();
    }
}
