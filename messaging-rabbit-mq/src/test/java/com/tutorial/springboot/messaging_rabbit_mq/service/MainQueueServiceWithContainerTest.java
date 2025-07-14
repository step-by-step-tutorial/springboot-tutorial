package com.tutorial.springboot.messaging_rabbit_mq.service;

import com.tutorial.springboot.messaging_rabbit_mq.model.MessageModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("unit tests of rabbitmq main queue service")
class MainQueueServiceWithContainerTest {

    @Autowired
    MainQueueService systemUnderTest;

    @Test
    @DisplayName("should throw a NullPointerException when given message is null")
    void shouldThrowNullPointerExceptionWhenMessageIsNull() {
        final MessageModel givenModel = null;

        final var expectedException = NullPointerException.class;
        final var expectedExceptionMessage = "model should not be null";

        final var actual = assertThrows(expectedException, () -> systemUnderTest.publish(givenModel));

        assertNotNull(actual);
        assertEquals(expectedExceptionMessage, actual.getMessage());
    }

    @Test
    @DisplayName("the message should be pushed to the queue")
    void messageShouldBePushedToTheQueue() {
        final var givenModel = new MessageModel(UUID.randomUUID().toString(), "test text");

        assertDoesNotThrow(() -> systemUnderTest.publish(givenModel));
    }

}
