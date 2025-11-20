package com.tutorial.springboot.streaming_kafka.listener;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles({"test"})
class SinkTopicListenerTest {

    @InjectMocks
    SinkTopicListener systemUnderTest;

    @Test
    void shouldThrowNullPointerExceptionWhenMessageIsNull() {
        final String givenMessage = null;
        final var givenCorrelationId = "fake correlation Id";

        final var expectedException = NullPointerException.class;
        final var expectedExceptionMessage = "Message should not be null";

        final var actual = assertThrows(expectedException, () -> systemUnderTest.onMessage(givenMessage, givenCorrelationId));

        assertNotNull(actual);
        assertEquals(expectedExceptionMessage, actual.getMessage());
    }

    @Test
    void shouldThrowNullPointerExceptionWhenCorrelationIdIsNull() {
        final var givenMessage = "fake message";
        final String givenCorrelationId = null;

        final var expectedException = NullPointerException.class;
        final var expectedExceptionMessage = "Correlation Id should not be null";

        final var actual = assertThrows(expectedException, () -> systemUnderTest.onMessage(givenMessage, givenCorrelationId));

        assertNotNull(actual);
        assertEquals(expectedExceptionMessage, actual.getMessage());
    }

    @Test
    void shouldSendAcceptedStatusWhenTheMessageWasProcessedSuccessful() {
        final var givenMessage = "fake message";
        final var givenCorrelationId = "fake correlation Id";

        assertDoesNotThrow(() -> systemUnderTest.onMessage(givenMessage, givenCorrelationId));
    }

}