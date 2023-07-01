package com.tutorial.springboot.messaging_artemis_mq.service;

import com.tutorial.springboot.messaging_artemis_mq.StubData;
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
@DisplayName("unit tests of status queue client")
class StatusQueueServiceTest {

    @InjectMocks
    private StatusQueueService systemUnderTest;

    @Mock
    private JmsTemplate jmsTemplate;

    private MockedStatic<MessageUtils> messageUtils;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(systemUnderTest, "destination", "fake queue");
        messageUtils = mockStatic(MessageUtils.class);
    }

    @AfterEach
    void tearDown() {
        messageUtils.close();
    }

    @Test
    @DisplayName("should throw a NullPointerException if the message is null")
    void shouldThrowNullPointerExceptionIfMessageIsNull() {
        final var givenMessage = StubData.NULL_STATUS_MODEL;

        final var expectedException = NullPointerException.class;
        final var expectedExceptionMessage = "model should not be null";

        final var actual = assertThrows(expectedException, () -> systemUnderTest.push(givenMessage));

        assertNotNull(actual);
        assertEquals(expectedExceptionMessage, actual.getMessage());
        verify(jmsTemplate, times(0)).send(anyString(), any());
    }

    @Test
    @DisplayName("the message should be pushed to the queue")
    void messageShouldBePushedToTheQueue() {
        final var givenMessage = StubData.FAKE_STATUS_MODEL;
        final var givenObjectMessage = mock(MessageCreator.class);
        final var givenDestination = "fake queue";
        messageUtils.when(() -> createSerializableMessage(any())).thenReturn(givenObjectMessage);
        doNothing()
                .when(jmsTemplate)
                .send(givenDestination, givenObjectMessage);

        assertDoesNotThrow(() -> systemUnderTest.push(givenMessage));

        messageUtils.verify(() -> createSerializableMessage(any()), times(1));
        verify(jmsTemplate, times(1)).send(givenDestination, givenObjectMessage);
    }
}