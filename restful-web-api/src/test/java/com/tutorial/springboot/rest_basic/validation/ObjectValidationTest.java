package com.tutorial.springboot.rest_basic.validation;

import com.tutorial.springboot.rest_basic.exception.ValidationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.tutorial.springboot.rest_basic.validation.ObjectValidation.*;
import static org.junit.jupiter.api.Assertions.*;

public class ObjectValidationTest {

    @Nested
    class shouldNotBeNull {

        @Test
        public void givenNonNullObject_thenContinue() {
            var givenObject = new Object();
            assertDoesNotThrow(() -> shouldNotBeNull(givenObject, "Object should not be empty"));
        }

        @Test
        public void givenNullObject_thenThrowValidationException() {
            final Object givenObject = null;
            var actual = assertThrows(ValidationException.class, () -> shouldNotBeNull(givenObject, "Object should not be empty"));
            assertFalse(actual.getMessage().isEmpty());
        }
    }

    @Nested
    class ShouldNotBeNullOrEmpty {

        @Test
        public void givenNonEmptyArray_thenContinue() {
            var givenArray = new String[]{"fake item"};
            assertDoesNotThrow(() -> shouldNotBeNullOrEmpty(givenArray, "Array should not be empty"));
        }

        @Test
        public void givenEmptyArray_thenThrowValidationException() {
            var givenArray = new Object[]{};
            var actual = assertThrows(ValidationException.class, () -> shouldNotBeNullOrEmpty(givenArray, "Array should not be empty"));
            assertFalse(actual.getMessage().isEmpty());
        }

        @Test
        public void givenNullArray_thenThrowValidationException() {
            final Object[] givenArray = null;
            var actual = assertThrows(ValidationException.class, () -> shouldNotBeNullOrEmpty(givenArray, "Array should not be empty"));
            assertFalse(actual.getMessage().isEmpty());
        }

    }

    @Nested
    class shouldBeEqual {

        @Test
        void givenEqualNumbers_thenContinue() {
            var givenN = 1L;
            var givenM = 1L;
            var givenErrorMessage = "fake";

            assertDoesNotThrow(() -> shouldBeEqual(givenN, givenM, givenErrorMessage));
        }

        @Test
        void givenUnequalNumbers_thenThrowValidationException() {
            var givenN = 1L;
            var givenM = 2L;
            var givenErrorMessage = "error message";

            var actual = assertThrows(ValidationException.class, () -> shouldBeEqual(givenN, givenM, givenErrorMessage));

            assertFalse(actual.getMessage().isEmpty());
        }
    }
}