package com.tutorial.springboot.artemismq.service;

import com.tutorial.springboot.artemismq.model.MessageModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles({"test", "embedded-artemis"})
class MainQueueServiceWithEmbeddedQueueTest {

    @Autowired
    MainQueueService systemUnderTest;

    @Test
    void shouldThrowNullPointerExceptionWhenMessageIsNull() {
        final MessageModel givenModel = null;

        final var expectedException = NullPointerException.class;
        final var expectedExceptionMessage = "MessageModel should not be null";

        final var actual = assertThrows(expectedException, () -> systemUnderTest.push(givenModel));

        assertNotNull(actual);
        assertEquals(expectedExceptionMessage, actual.getMessage());
    }

    @Test
    void messageShouldBePushedToTheQueue() {
        final var givenModel = new MessageModel(UUID.randomUUID().toString(), "test text");

        assertDoesNotThrow(() -> systemUnderTest.push(givenModel));
    }

}
