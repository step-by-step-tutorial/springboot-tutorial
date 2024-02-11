package com.tutorial.springboot.rest_basic.util;

import com.tutorial.springboot.rest_basic.exception.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;

public final class ApiErrorUtils {

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private ApiErrorUtils() {
    }

    public static void checkValidation(BindingResult bindingResult) {
        requireNonNull(bindingResult, "BindingResult should not be null");
        if (bindingResult.hasErrors()) {
            var errorMessages = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            throw new ValidationException(errorMessages);
        }
    }

    @SafeVarargs
    public static <T> void checkValidation(T... array) {
        var errors = Stream.of(array)
                .filter(Objects::nonNull)
                .map(validator::validate)
                .flatMap(Collection::stream)
                .map(ConstraintViolation::getMessage)
                .collect(toSet());

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
