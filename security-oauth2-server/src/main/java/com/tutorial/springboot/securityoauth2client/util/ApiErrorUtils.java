package com.tutorial.springboot.securityoauth2client.util;

import com.tutorial.springboot.securityoauth2client.exception.ValidationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;

import static java.util.Objects.requireNonNull;

public final class ApiErrorUtils {

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
}
