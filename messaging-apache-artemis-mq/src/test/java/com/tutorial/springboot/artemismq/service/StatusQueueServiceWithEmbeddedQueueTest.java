package com.tutorial.springboot.artemismq.service;

import com.tutorial.springboot.artemismq.model.Acknowledge;
import com.tutorial.springboot.artemismq.model.StatusModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles({"test", "embedded-artemis"})
class StatusQueueServiceWithEmbeddedQueueTest {

    @Autowired
    StatusQueueService systemUnderTest;

    @Test
    void shouldThrowNullPointerExceptionWhenMessageIsNull() {
        final StatusModel givenModel = null;

        final var expectedException = NullPointerException.class;
        final var expectedExceptionMessage = "StatusModel should not be null";

        final var actual = assertThrows(expectedException, () -> systemUnderTest.push(givenModel));

        assertNotNull(actual);
        assertEquals(expectedExceptionMessage, actual.getMessage());
    }

    @Test
    void messageShouldBePushedToTheQueue() {
        final var givenModel = new StatusModel(Acknowledge.ACCEPTED, "test additional data", "");

        assertDoesNotThrow(() -> systemUnderTest.push(givenModel));
    }

}
