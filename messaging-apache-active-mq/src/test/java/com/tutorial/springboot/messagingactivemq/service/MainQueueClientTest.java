package com.tutorial.springboot.messagingactivemq.service;

import com.tutorial.springboot.messagingactivemq.StubData;
import com.tutorial.springboot.messagingactivemq.utils.MessageUtils;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.TestPropertySources;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static com.tutorial.springboot.messagingactivemq.StubData.FAKE_MESSAGE;
import static com.tutorial.springboot.messagingactivemq.utils.MessageUtils.createSerializableMessage;
import static com.tutorial.springboot.messagingactivemq.utils.MessageUtils.extractBody;
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

    MockedStatic<MessageUtils> messageUtils;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(underTest, "destination", "fake queue");
        messageUtils = mockStatic(MessageUtils.class);
    }

    @AfterEach
    void tearDown() {
        messageUtils.close();
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
        var givenMessageCreator = mock(MessageCreator.class);
        messageUtils.when(() -> createSerializableMessage(any())).thenReturn(givenMessageCreator);

        doNothing()
                .when(jmsTemplate)
                .send("fake queue", givenMessageCreator);

        assertDoesNotThrow(() -> underTest.push(givenMessage));

        messageUtils.verify(() -> createSerializableMessage(any()), times(1));
        verify(jmsTemplate, times(1)).send("fake queue", givenMessageCreator);
    }

}
