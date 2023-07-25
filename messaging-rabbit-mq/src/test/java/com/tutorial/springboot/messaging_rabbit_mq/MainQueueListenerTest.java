package com.tutorial.springboot.messaging_rabbit_mq;

import com.tutorial.springboot.messaging_rabbit_mq.listener.MainQueueListener;
import com.tutorial.springboot.messaging_rabbit_mq.model.MessageModel;
import com.tutorial.springboot.messaging_rabbit_mq.utils.MessageUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Message;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static com.tutorial.springboot.messaging_rabbit_mq.utils.MessageUtils.extractBody;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles({"test"})
@DisplayName("unit tests of rabbitmq main queue listener")
class MainQueueListenerTest {

    @InjectMocks
    MainQueueListener systemUnderTest;

    MockedStatic<MessageUtils> messageUtils;

    @BeforeEach
    void setUp() {
        messageUtils = mockStatic(MessageUtils.class);
    }

    @AfterEach
    void tearDown() {
        messageUtils.close();
    }

    @Test
    @DisplayName("should throw a NullPointerException when given message is null")
    void shouldThrowNullPointerExceptionWhenMessageIsNull() {
        final Message givenMessage = null;
        final var givenCorrelationId = "fake correlation Id";

        final var expectedException = NullPointerException.class;
        final var expectedExceptionMessage = "message should not be null";

        final var actual = assertThrows(expectedException, () -> systemUnderTest.onMessage(givenMessage, givenCorrelationId));

        assertNotNull(actual);
        assertEquals(expectedExceptionMessage, actual.getMessage());
    }

    @Test
    @DisplayName("should throw a NullPointerException when given correlation is null")
    void shouldThrowNullPointerExceptionWhenCorrelationIdIsNull() {
        final var givenMessage = new Message("fake body".getBytes());
        final String givenCorrelationId = null;

        final var expectedException = NullPointerException.class;
        final var expectedExceptionMessage = "correlation Id should not be null";

        final var actual = assertThrows(expectedException, () -> systemUnderTest.onMessage(givenMessage, givenCorrelationId));

        assertNotNull(actual);
        assertEquals(expectedExceptionMessage, actual.getMessage());
    }

    @Test
    @DisplayName("should processes message successful")
    void shouldSendAcceptedStatusWhenTheMessageWasProcessedSuccessful() {
        final var givenMessage = mock(Message.class);
        final var givenCorrelationId = "fake correlation Id";
        final var givenBody = Optional.of(new MessageModel("fake Id", "fake text"));
        messageUtils.when(() -> extractBody(any(), any())).thenReturn(givenBody);

        assertDoesNotThrow(() -> systemUnderTest.onMessage(givenMessage, givenCorrelationId));

        messageUtils.verify(() -> extractBody(any(), any()), times(1));
    }

}