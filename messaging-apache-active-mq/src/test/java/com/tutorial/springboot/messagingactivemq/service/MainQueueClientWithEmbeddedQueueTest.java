package com.tutorial.springboot.messagingactivemq.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles({"test", "embedded"})
@DisplayName("unit test of main queue client")
class MainQueueClientWithEmbeddedQueueTest {

    @Autowired
    private MainQueueClient underTest;

    @Test
    @DisplayName("should throw NullPointerException if message is null")
    void shouldThrowNullPointerExceptionIfMessageIsNull() {
        var givenMessage = StubData.NULL_MESSAGE;

        var expectedException = NullPointerException.class;
        var expectedExceptionMessage = "message should not be null";

        var actual = assertThrows(expectedException, () -> underTest.publish(givenMessage));

        assertNotNull(actual);
        assertEquals(expectedExceptionMessage, actual.getMessage());
    }

    @Test
    @DisplayName("message should be pushed to the queue")
    void messageShouldBePushedToTheQueue() {
        var givenMessage = StubData.FAKE_MESSAGE;

        assertDoesNotThrow(() -> underTest.publish(givenMessage));
    }

}
