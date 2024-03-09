package com.tutorial.springboot.rest_basic.util;

import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Clean Up Utilities Unit Tests")
class CleanUpUtilsTest {

    private record StubDto(@NotNull Integer id) {
    }

    @Test
    @DisplayName("Test with null objects only")
    void givenNullObjectsOnly_whenClean_thenExpectZeroCount() {
        var givenStream = Stream.of(null, null);
        var actual = CleanUpUtils.clean(givenStream);
        assertEquals(0, actual.count());
    }

    @Test
    @DisplayName("Test with null and non-null objects")
    void givenNullAndNonNullObjects_whenClean_thenExpectCountOfNotNullObjects() {
        var givenStream = Stream.of(new StubDto(1), null);
        var actual = CleanUpUtils.clean(givenStream);
        assertEquals(1, actual.count());
    }

    @Test
    @DisplayName("Test with valid and invalid objects")
    void givenValidAndInvalidObjects_whenClean_thenExpectCountOfValidObjects() {
        var givenStream = Stream.of(new StubDto(1), new StubDto(null));
        var actual = CleanUpUtils.clean(givenStream);
        assertEquals(1, actual.count());
    }

    @Test
    @DisplayName("Test with only invalid objects")
    void givenOnlyInvalidObjects_whenClean_thenExpectZeroCount() {
        var givenStream = Stream.of(new StubDto(null), new StubDto(null));
        var actual = CleanUpUtils.clean(givenStream);
        assertEquals(0, actual.count());
    }

}