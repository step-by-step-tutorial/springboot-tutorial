package com.tutorial.springboot.restful_web_api.validation;

import com.tutorial.springboot.restful_web_api.exception.ValidationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;

import static java.util.Objects.requireNonNull;

public final class ApiValidation {

    private ApiValidation() {
    }

    public static void shouldBeValid(BindingResult bindingResult) {
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
