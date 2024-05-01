package com.tutorial.springboot.messaging_artemis_mq.service;

import com.tutorial.springboot.messaging_artemis_mq.model.MessageModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles({"test", "embedded-artemis"})
@DisplayName("unit tests of artemis[embedded] main queue service")
class MainQueueServiceWithEmbeddedQueueTest {

    @Autowired
    MainQueueService systemUnderTest;

    @Test
    @DisplayName("should throw a NullPointerException when given message is null")
    void shouldThrowNullPointerExceptionWhenMessageIsNull() {
        final MessageModel givenModel = null;

        final var expectedException = NullPointerException.class;
        final var expectedExceptionMessage = "model should not be null";

        final var actual = assertThrows(expectedException, () -> systemUnderTest.push(givenModel));

        assertNotNull(actual);
        assertEquals(expectedExceptionMessage, actual.getMessage());
    }

    @Test
    @DisplayName("the message should be pushed to the queue")
    void messageShouldBePushedToTheQueue() {
        final var givenModel = new MessageModel(UUID.randomUUID().toString(), "test text");

        assertDoesNotThrow(() -> systemUnderTest.push(givenModel));
    }

}
