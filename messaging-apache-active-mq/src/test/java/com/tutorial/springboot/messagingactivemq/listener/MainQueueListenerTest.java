package com.tutorial.springboot.messagingactivemq.listener;

import com.tutorial.springboot.messagingactivemq.StubData;
import com.tutorial.springboot.messagingactivemq.service.StatusQueueClient;
import com.tutorial.springboot.messagingactivemq.utils.MessageUtils;
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

import static com.tutorial.springboot.messagingactivemq.StubData.FAKE_MESSAGE;
import static com.tutorial.springboot.messagingactivemq.utils.MessageUtils.extractBody;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles({"test"})
@DisplayName("unit test of main queue listener")
class MainQueueListenerTest {

    @InjectMocks
    private MainQueueListener underTest;

    @Mock
    private StatusQueueClient statusQueueClient;

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
    @DisplayName("should throw NullPointerException if message is null")
    void shouldThrowNullPointerExceptionIfMessageIsNull() {
        var givenMessage = StubData.NULL_JMS_MESSAGE;

        var expectedException = NullPointerException.class;
        var expectedExceptionMessage = "message should not be null";

        var actual = assertThrows(expectedException, () -> underTest.onMessage(givenMessage));

        assertNotNull(actual);
        assertEquals(expectedExceptionMessage, actual.getMessage());
        verify(statusQueueClient, times(0)).push(any());
    }

    @Test
    @DisplayName("should send ACCEPTED status if the message was processed successful")
    void shouldSendAcceptedStatusIfTheMessageWasProcessedSuccessful() {
        var givenMessage = mock(Message.class);
        messageUtils.when(() -> extractBody(any(), any())).thenReturn(Optional.of(FAKE_MESSAGE));

        assertDoesNotThrow(() -> underTest.onMessage(givenMessage));

        messageUtils.verify(() -> extractBody(any(), any()), times(1));
        verify(statusQueueClient, times(1)).push(any());
    }

    @Test
    @DisplayName("should send FAILED status if the message was not processed successful")
    void shouldSendFailedStatusIfTheMessageWasNotProcessedSuccessful() {
        var givenMessage = mock(Message.class);
        messageUtils.when(() -> extractBody(any(), any())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> underTest.onMessage(givenMessage));

        messageUtils.verify(() -> extractBody(any(), any()), times(1));
        verify(statusQueueClient, times(1)).push(any());
    }

}