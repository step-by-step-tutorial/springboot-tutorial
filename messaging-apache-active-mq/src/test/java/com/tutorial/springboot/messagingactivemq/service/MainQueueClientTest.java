package com.tutorial.springboot.messagingactivemq.service;

import com.tutorial.springboot.messagingactivemq.StubData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles({"test"})
@DisplayName("unit test of main queue client")
class MainQueueClientTest {

    @InjectMocks
    private MainQueueClient underTest;

    @Mock
    private JmsTemplate jmsTemplate;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(underTest, "destination", "fake queue");
    }

    @Test
    @DisplayName("should throw NullPointerException if message is null")
    void shouldThrowNullPointerExceptionIfMessageIsNull() {
        var givenMessage = StubData.NULL_MESSAGE;

        var expectedException = NullPointerException.class;
        var expectedExceptionMessage = "message should not be null";

        var actual = assertThrows(expectedException, () -> underTest.push(givenMessage));

        assertNotNull(actual);
        assertEquals(expectedExceptionMessage, actual.getMessage());
        verify(jmsTemplate, times(0)).send(anyString(), any());
    }

    @Test
    @DisplayName("message should be pushed to the queue")
    void messageShouldBePushedToTheQueue() {
        var givenMessage = StubData.FAKE_MESSAGE;

        doNothing()
                .when(jmsTemplate)
                .send(anyString(), any());

        underTest.push(givenMessage);

        verify(jmsTemplate, times(1)).send(anyString(), any());
    }

}
