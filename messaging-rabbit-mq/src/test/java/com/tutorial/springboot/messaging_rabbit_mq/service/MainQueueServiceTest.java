package com.tutorial.springboot.messaging_rabbit_mq.service;

import com.tutorial.springboot.messaging_rabbit_mq.service.MainQueueService;
import com.tutorial.springboot.messaging_rabbit_mq.utils.MessageUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import static com.tutorial.springboot.messaging_rabbit_mq.utils.MessageUtils.createMessage;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles({"test"})
@DisplayName("unit tests of rabbitmq main queue service")
class MainQueueServiceTest {

    @InjectMocks
    MainQueueService systemUnderTest;

    @Mock
    RabbitTemplate rabbitTemplate;

    MockedStatic<MessageUtils> messageUtils;

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
    @DisplayName("should throw NullPointerException when given message is null")
    void shouldThrowNullPointerExceptionWhenMessageIsNull() {
        final Object givenMessage = null;

        final var expectedException = NullPointerException.class;
        final var expectedExceptionMessage = "model should not be null";

        final var actual = assertThrows(expectedException, () -> systemUnderTest.push(givenMessage));

        assertNotNull(actual);
        assertEquals(expectedExceptionMessage, actual.getMessage());
        verify(rabbitTemplate, times(0)).send(anyString(), any());
    }

    @Test
    @DisplayName("the message should be pushed to the queue")
    void messageShouldBePushedToTheQueue() {
        final var givenMessage = "fake message";

        final var fakeByteMessage = mock(Message.class);
        final var fakeDestination = "fake queue";
        messageUtils.when(() -> createMessage(any())).thenReturn(fakeByteMessage);
        doNothing().when(rabbitTemplate).send(fakeDestination, fakeByteMessage);

        assertDoesNotThrow(() -> systemUnderTest.push(givenMessage));

        verify(rabbitTemplate, times(1)).send(fakeDestination, fakeByteMessage);
        messageUtils.verify(() -> createMessage(any()), times(1));
    }

}
