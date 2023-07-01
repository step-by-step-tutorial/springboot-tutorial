package com.tutorial.springboot.messaging_artemis_mq.listener;

import com.tutorial.springboot.messaging_artemis_mq.StubData;
import com.tutorial.springboot.messaging_artemis_mq.model.Acknowledge;
import com.tutorial.springboot.messaging_artemis_mq.model.StatusModel;
import com.tutorial.springboot.messaging_artemis_mq.service.StatusQueueService;
import com.tutorial.springboot.messaging_artemis_mq.utils.MessageUtils;
import jakarta.jms.Message;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
@DisplayName("unit tests of main queue listener")
class MainQueueListenerTest {

    @InjectMocks
    private MainQueueListener systemUnderTest;

    @Mock
    private StatusQueueService statusQueueService;

    private MockedStatic<MessageUtils> messageUtils;

    @BeforeEach
    void setUp() {
        messageUtils = mockStatic(MessageUtils.class);
    }

    @AfterEach
    void tearDown() {
        messageUtils.close();
    }

    @Test
    @DisplayName("should throw a NullPointerException if the message is null")
    void shouldThrowNullPointerExceptionIfMessageIsNull() {
        final var givenMessage = StubData.NULL_JMS_MESSAGE;

        final var expectedException = NullPointerException.class;
        final var expectedExceptionMessage = "message should not be null";

        final var actual = assertThrows(expectedException, () -> systemUnderTest.onMessage(givenMessage));

        assertNotNull(actual);
        assertEquals(expectedExceptionMessage, actual.getMessage());
        verify(statusQueueService, times(0)).push(any());
    }

    @Test
    @DisplayName("should send an ACCEPTED status if the message was processed successful")
    void shouldSendAcceptedStatusIfTheMessageWasProcessedSuccessful() {
        final var givenMessage = mock(Message.class);
        final var givenBody = Optional.of(StubData.FAKE_MESSAGE_Model);
        final var givenCorrelationId = "fake correlation Id";
        messageUtils.when(() -> extractBody(any(), any())).thenReturn(givenBody);
        messageUtils.when(() -> extractCorrelationId(any())).thenReturn(givenCorrelationId);
        doNothing()
                .when(statusQueueService)
                .push(new StatusModel(Acknowledge.ACCEPTED, givenCorrelationId));

        assertDoesNotThrow(() -> systemUnderTest.onMessage(givenMessage));

        messageUtils.verify(() -> extractBody(any(), any()), times(1));
        messageUtils.verify(() -> extractCorrelationId(any()), times(1));
        verify(statusQueueService, times(1))
                .push(new StatusModel(Acknowledge.ACCEPTED, givenCorrelationId));
    }

    @Test
    @DisplayName("should send a FAILED status if the message was not processed successful")
    void shouldSendFailedStatusIfTheMessageWasNotProcessedSuccessful() {
        final var givenMessage = mock(Message.class);
        final var givenBody = Optional.empty();
        messageUtils.when(() -> extractBody(any(), any())).thenReturn(givenBody);
        messageUtils.when(() -> extractCorrelationId(any())).thenReturn("fake correlation Id");
        doNothing()
                .when(statusQueueService)
                .push(new StatusModel(Acknowledge.FAILED, "fake correlation Id"));

        assertDoesNotThrow(() -> systemUnderTest.onMessage(givenMessage));

        messageUtils.verify(() -> extractBody(any(), any()), times(1));
        messageUtils.verify(() -> extractCorrelationId(any()), times(1));
        verify(statusQueueService, times(1)).push(new StatusModel(Acknowledge.FAILED, "fake correlation Id"));
    }

}