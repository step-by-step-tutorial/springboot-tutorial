package com.tutorial.springboot.messaging_rabbit_mq.service;

import com.tutorial.springboot.messaging_rabbit_mq.model.MessageModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MainQueueServiceWithContainerTest {

    @Autowired
    MainQueueService systemUnderTest;

    @Test
    void shouldThrowNullPointerExceptionWhenMessageIsNull() {
        final MessageModel givenModel = null;

        final var expectedException = NullPointerException.class;
        final var expectedExceptionMessage = "Model should not be null";

        final var actual = assertThrows(expectedException, () -> systemUnderTest.publish(givenModel));

        assertNotNull(actual);
        assertEquals(expectedExceptionMessage, actual.getMessage());
    }

    @Test
    void messageShouldBePushedToTheQueue() {
        final var givenModel = new MessageModel(UUID.randomUUID().toString(), "test text");

        assertDoesNotThrow(() -> systemUnderTest.publish(givenModel));
    }

}
