package com.tutorial.springboot.messaging_artemis_mq.service;

import com.tutorial.springboot.messaging_artemis_mq.StubData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles({"test", "embedded"})
@DisplayName("unit tests of status queue client")
class StatusQueueClientWithEmbeddedQueueTest {

    @Autowired
    private StatusQueueClient systemUnderTest;

    @Test
    @DisplayName("should throw NullPointerException if the message is null")
    void shouldThrowNullPointerExceptionIfMessageIsNull() {
        var givenMessage = StubData.NULL_STATUS_MODEL;

        var expectedException = NullPointerException.class;
        var expectedExceptionMessage = "message should not be null";

        var actual = assertThrows(expectedException, () -> systemUnderTest.push(givenMessage));

        assertNotNull(actual);
        assertEquals(expectedExceptionMessage, actual.getMessage());
    }

    @Test
    @DisplayName("the message should be pushed to the queue")
    void messageShouldBePushedToTheQueue() {
        var givenMessage = StubData.FAKE_STATUS_MODEL;

        assertDoesNotThrow(() -> systemUnderTest.push(givenMessage));
    }

}
