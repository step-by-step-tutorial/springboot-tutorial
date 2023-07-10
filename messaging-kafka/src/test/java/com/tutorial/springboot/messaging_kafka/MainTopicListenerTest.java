package com.tutorial.springboot.messaging_kafka;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles({"test"})
@DisplayName("unit tests of main topic listener")
class MainTopicListenerTest {

    @InjectMocks
    MainTopicListener systemUnderTest;

    @Test
    @DisplayName("should throw a NullPointerException if the message is null")
    void shouldThrowNullPointerExceptionIfMessageIsNull() {
        final String givenMessage = null;
        final var givenCorrelationId = "fake correlation Id";

        final var expectedException = NullPointerException.class;
        final var expectedExceptionMessage = "message should not be null";

        final var actual = assertThrows(expectedException, () -> systemUnderTest.onMessage(givenMessage, givenCorrelationId));

        assertNotNull(actual);
        assertEquals(expectedExceptionMessage, actual.getMessage());
    }

    @Test
    @DisplayName("should throw a NullPointerException when the correlation is null")
    void shouldThrowNullPointerExceptionWhenCorrelationIdIsNull() {
        final var givenMessage = "fake message";
        final String givenCorrelationId = null;

        final var expectedException = NullPointerException.class;
        final var expectedExceptionMessage = "correlation Id should not be null";

        final var actual = assertThrows(expectedException, () -> systemUnderTest.onMessage(givenMessage, givenCorrelationId));

        assertNotNull(actual);
        assertEquals(expectedExceptionMessage, actual.getMessage());
    }

    @Test
    @DisplayName("should processes message successful")
    void shouldSendAcceptedStatusIfTheMessageWasProcessedSuccessful() {
        final var givenMessage = "fake message";
        final var givenCorrelationId = "fake correlation Id";

        assertDoesNotThrow(() -> systemUnderTest.onMessage(givenMessage, givenCorrelationId));
    }

}