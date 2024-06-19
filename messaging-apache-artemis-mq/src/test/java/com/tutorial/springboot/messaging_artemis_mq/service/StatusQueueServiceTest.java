package com.tutorial.springboot.messaging_artemis_mq.service;

import com.tutorial.springboot.messaging_artemis_mq.model.Acknowledge;
import com.tutorial.springboot.messaging_artemis_mq.model.StatusModel;
import com.tutorial.springboot.messaging_artemis_mq.utils.MessageUtils;
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
import org.springframework.test.util.ReflectionTestUtils;

import static com.tutorial.springboot.messaging_artemis_mq.utils.MessageUtils.createSerializableMessage;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles({"test"})
@DisplayName("unit tests of artemis status queue service")
class StatusQueueServiceTest {

    @InjectMocks
    StatusQueueService systemUnderTest;

    @Mock
    JmsTemplate jmsTemplate;

    MockedStatic<MessageUtils> messageUtils;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(systemUnderTest, "queue", "fake queue");
        messageUtils = mockStatic(MessageUtils.class);
    }

    @AfterEach
    void tearDown() {
        messageUtils.close();
    }

    @Test
    @DisplayName("should throw a NullPointerException when given message is null")
    void shouldThrowNullPointerExceptionWhenMessageIsNull() {
        final StatusModel givenModel = null;

        final var expectedException = NullPointerException.class;
        final var expectedExceptionMessage = "StatusModel should not be null";

        final var actual = assertThrows(expectedException, () -> systemUnderTest.push(givenModel));

        assertNotNull(actual);
        assertEquals(expectedExceptionMessage, actual.getMessage());
        verify(jmsTemplate, times(0)).send(anyString(), any());
    }

    @Test
    @DisplayName("the message should be pushed to the queue")
    void messageShouldBePushedToTheQueue() {
        final var givenModel = new StatusModel(Acknowledge.ACCEPTED, "fake additional data", "");
        final var givenObjectMessage = mock(MessageCreator.class);
        final var givenDestination = "fake queue";
        messageUtils.when(() -> createSerializableMessage(any())).thenReturn(givenObjectMessage);
        doNothing()
                .when(jmsTemplate)
                .send(givenDestination, givenObjectMessage);

        assertDoesNotThrow(() -> systemUnderTest.push(givenModel));

        messageUtils.verify(() -> createSerializableMessage(any()), times(1));
        verify(jmsTemplate, times(1)).send(givenDestination, givenObjectMessage);
    }
}