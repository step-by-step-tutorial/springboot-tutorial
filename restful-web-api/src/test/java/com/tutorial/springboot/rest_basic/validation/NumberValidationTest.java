package com.tutorial.springboot.rest_basic.validation;

import com.tutorial.springboot.rest_basic.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Number Validation Unit Tests")
class NumberValidationTest {

    @Test
    void givenEqualNumbers_thenContinue() {
        var givenN = 1L;
        var givenM = 1L;
        var givenErrorMessage = "fake";

        assertDoesNotThrow(() -> NumberValidation.requireEquality(givenN, givenM, givenErrorMessage));
    }

    @Test
    void givenUnequalNumbers_thenThrowValidationException() {
        var givenN = 1L;
        var givenM = 2L;
        var givenErrorMessage = "error message";

        var actual = assertThrows(ValidationException.class, () -> NumberValidation.requireEquality(givenN, givenM, givenErrorMessage));

        assertFalse(actual.getMessage().isEmpty());
    }
}