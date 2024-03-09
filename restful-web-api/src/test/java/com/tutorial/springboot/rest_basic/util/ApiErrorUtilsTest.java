package com.tutorial.springboot.rest_basic.util;

import com.tutorial.springboot.rest_basic.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Error Utils Unit Tests")
class ApiErrorUtilsTest {

    @Test
    @DisplayName("Pass validation with no error")
    void givenNoErrors_thenValidationPasses() {
        var givenBindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(givenBindingResult.hasErrors()).thenReturn(false);
        assertDoesNotThrow(() -> ApiErrorUtils.checkValidation(givenBindingResult));
    }

    @Test
    @DisplayName("Failed validation with errors")
    void givenValidationErrors_thenThrowsException() {
        var givenBindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(givenBindingResult.hasErrors()).thenReturn(true);
        Mockito.when(givenBindingResult.getAllErrors()).thenReturn(List.of(new ObjectError("object name", "error message")));

        var actual = assertThrows(ValidationException.class, () -> ApiErrorUtils.checkValidation(givenBindingResult));

        assertNotNull(actual);
        assertNotNull(actual.getMessage());
        assertFalse(actual.getDetails().isEmpty());
    }
}