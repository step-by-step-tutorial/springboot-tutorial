package com.tutorial.springboot.messaging_artemis_mq.service;

import com.tutorial.springboot.messaging_artemis_mq.model.Acknowledge;
import com.tutorial.springboot.messaging_artemis_mq.model.StatusModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles({"test", "embedded-artemis"})
@DisplayName("unit tests of artemis[embedded] status queue service")
class StatusQueueServiceWithEmbeddedQueueTest {

    @Autowired
    StatusQueueService systemUnderTest;

    @Test
    @DisplayName("should throw NullPointerException when given message is null")
    void shouldThrowNullPointerExceptionWhenMessageIsNull() {
        final StatusModel givenModel = null;

        final var expectedException = NullPointerException.class;
        final var expectedExceptionMessage = "model should not be null";

        final var actual = assertThrows(expectedException, () -> systemUnderTest.push(givenModel));

        assertNotNull(actual);
        assertEquals(expectedExceptionMessage, actual.getMessage());
    }

    @Test
    @DisplayName("the message should be pushed to the queue")
    void messageShouldBePushedToTheQueue() {
        final var givenModel = new StatusModel(Acknowledge.ACCEPTED, "test additional data");

        assertDoesNotThrow(() -> systemUnderTest.push(givenModel));
    }

}
