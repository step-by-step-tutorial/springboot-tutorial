package com.tutorial.springboot.artemismq.listener;

import com.tutorial.springboot.artemismq.model.Acknowledge;
import com.tutorial.springboot.artemismq.model.MessageModel;
import com.tutorial.springboot.artemismq.model.StatusModel;
import com.tutorial.springboot.artemismq.service.StatusQueueService;
import com.tutorial.springboot.artemismq.utils.MessageUtils;
import jakarta.jms.Message;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static com.tutorial.springboot.artemismq.utils.MessageUtils.extractBody;
import static com.tutorial.springboot.artemismq.utils.MessageUtils.extractCorrelationId;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles({"test"})
class MainQueueListenerTest {

    @InjectMocks
    MainQueueListener systemUnderTest;

    @Mock
    StatusQueueService statusQueueService;

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
    void shouldThrowNullPointerExceptionWhenMessageIsNull() {
        final Message givenMessage = null;

        final var expectedException = NullPointerException.class;
        final var expectedExceptionMessage = "Message should not be null";

        final var actual = assertThrows(expectedException, () -> systemUnderTest.onMessage(givenMessage));

        assertNotNull(actual);
        assertEquals(expectedExceptionMessage, actual.getMessage());
        verify(statusQueueService, times(0)).push(any());
    }

    @Test
    void shouldSendAcceptedStatusWhenTheMessageWasProcessedSuccessful() {
        final var givenMessage = mock(Message.class);
        final var givenBody = Optional.of(new MessageModel("fake Id", "fake text"));
        final var givenCorrelationId = "fake correlation Id";
        messageUtils.when(() -> extractBody(any(), any())).thenReturn(givenBody);
        messageUtils.when(() -> extractCorrelationId(any())).thenReturn(givenCorrelationId);
        doNothing()
                .when(statusQueueService)
                .push(new StatusModel(Acknowledge.ACCEPTED, givenCorrelationId, ""));

        assertDoesNotThrow(() -> systemUnderTest.onMessage(givenMessage));

        messageUtils.verify(() -> extractBody(any(), any()), times(1));
        messageUtils.verify(() -> extractCorrelationId(any()), times(1));
        verify(statusQueueService, times(1))
                .push(new StatusModel(Acknowledge.ACCEPTED, givenCorrelationId, ""));
    }

    @Test
    void shouldSendFailedStatusWhenTheMessageWasNotProcessedSuccessful() {
        final var givenMessage = mock(Message.class);
        final var givenBody = Optional.empty();
        messageUtils.when(() -> extractBody(any(), any())).thenReturn(givenBody);
        messageUtils.when(() -> extractCorrelationId(any())).thenReturn("fake correlation Id");
        doNothing()
                .when(statusQueueService)
                .push(new StatusModel(Acknowledge.FAILED, "fake correlation Id", "Message body could not be extracted"));

        assertDoesNotThrow(() -> systemUnderTest.onMessage(givenMessage));

        messageUtils.verify(() -> extractBody(any(), any()), times(1));
        messageUtils.verify(() -> extractCorrelationId(any()), times(1));
        verify(statusQueueService, times(1)).push(new StatusModel(Acknowledge.FAILED, "fake correlation Id", "Message body could not be extracted"));
    }

}