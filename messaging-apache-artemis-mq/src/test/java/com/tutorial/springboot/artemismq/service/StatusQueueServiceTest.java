package com.tutorial.springboot.artemismq.service;

import com.tutorial.springboot.artemismq.model.Acknowledge;
import com.tutorial.springboot.artemismq.model.StatusModel;
import com.tutorial.springboot.artemismq.utils.MessageUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

import static com.tutorial.springboot.artemismq.utils.MessageUtils.createSerializableMessage;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles({"test"})
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