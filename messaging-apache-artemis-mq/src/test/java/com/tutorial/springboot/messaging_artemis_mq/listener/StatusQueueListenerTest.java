package com.tutorial.springboot.messaging_artemis_mq.listener;

import com.tutorial.springboot.messaging_artemis_mq.model.Acknowledge;
import com.tutorial.springboot.messaging_artemis_mq.model.StatusModel;
import com.tutorial.springboot.messaging_artemis_mq.utils.MessageUtils;
import jakarta.jms.Message;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static com.tutorial.springboot.messaging_artemis_mq.utils.MessageUtils.extractBody;
import static com.tutorial.springboot.messaging_artemis_mq.utils.MessageUtils.extractCorrelationId;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles({"test"})
@DisplayName("unit tests of artemis status queue listener")
class StatusQueueListenerTest {

    @InjectMocks
    StatusQueueListener systemUnderTest;

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

        final var expectedException = NullPointerException.class;
        final var expectedExceptionMessage = "message should not be null";

        final var actual = assertThrows(expectedException, () -> systemUnderTest.onMessage(givenMessage));

        assertNotNull(actual);
        assertEquals(expectedExceptionMessage, actual.getMessage());
    }

    @Test
    @DisplayName("should log an info when given message was processed successful")
    void shouldLogInfoWhenTheMessageWasProcessedSuccessful() {
        final var givenMessage = mock(Message.class);
        final var givenBody = Optional.of(new StatusModel(Acknowledge.ACCEPTED, "fake additional data"));
        final var givenCorrelationId = "fake correlation Id";
        messageUtils.when(() -> extractBody(any(), any())).thenReturn(givenBody);
        messageUtils.when(() -> extractCorrelationId(any())).thenReturn(givenCorrelationId);

        assertDoesNotThrow(() -> systemUnderTest.onMessage(givenMessage));

        messageUtils.verify(() -> extractBody(any(), any()), times(1));
        messageUtils.verify(() -> extractCorrelationId(any()), times(1));
    }

    @Test
    @DisplayName("should logg an error when given message was not processed successful")
    void shouldLogErrorWhenTheMessageWasNotProcessedSuccessful() {
        final var givenMessage = mock(Message.class);
        final var givenBody = Optional.empty();
        final var givenCorrelationId = "fake correlation Id";
        messageUtils.when(() -> extractBody(any(), any())).thenReturn(givenBody);
        messageUtils.when(() -> extractCorrelationId(any())).thenReturn(givenCorrelationId);

        assertDoesNotThrow(() -> systemUnderTest.onMessage(givenMessage));

        messageUtils.verify(() -> extractBody(any(), any()), times(1));
        messageUtils.verify(() -> extractCorrelationId(any()), times(1));
    }

}