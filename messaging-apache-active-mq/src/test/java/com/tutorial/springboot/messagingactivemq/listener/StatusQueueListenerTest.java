package com.tutorial.springboot.messagingactivemq.listener;

import com.tutorial.springboot.messagingactivemq.StubData;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static com.tutorial.springboot.messagingactivemq.StubData.FAKE_STATUS;
import static com.tutorial.springboot.messagingactivemq.utils.MessageUtils.extractBody;
import static com.tutorial.springboot.messagingactivemq.utils.MessageUtils.extractCorrelationId;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles({"test"})
@DisplayName("unit test of status queue listener")
class StatusQueueListenerTest {

    @InjectMocks
    private StatusQueueListener underTest;

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
    @DisplayName("should throw NullPointerException if message is null")
    void shouldThrowNullPointerExceptionIfMessageIsNull() {
        var givenMessage = StubData.NULL_JMS_MESSAGE;

        var expectedException = NullPointerException.class;
        var expectedExceptionMessage = "message should not be null";

        var actual = assertThrows(expectedException, () -> underTest.onMessage(givenMessage));

        assertNotNull(actual);
        assertEquals(expectedExceptionMessage, actual.getMessage());
    }

    @Test
    @DisplayName("should logg as a info if the message was processed successful")
    void shouldLogAsInfoIfTheMessageWasProcessedSuccessful() {
        var givenMessage = mock(Message.class);
        messageUtils.when(() -> extractBody(any(), any())).thenReturn(Optional.of(FAKE_STATUS));
        messageUtils.when(() -> extractCorrelationId(any())).thenReturn("fake correlation Id");


        assertDoesNotThrow(() -> underTest.onMessage(givenMessage));

        messageUtils.verify(() -> extractBody(any(), any()), times(1));
        messageUtils.verify(() -> extractCorrelationId(any()), times(1));
    }

    @Test
    @DisplayName("should logg as a error if the message was not processed successful")
    void shouldLogAsErrorIfTheMessageWasNotProcessedSuccessful() {
        var givenMessage = mock(Message.class);
        messageUtils.when(() -> extractBody(any(), any())).thenReturn(Optional.empty());
        messageUtils.when(() -> extractCorrelationId(any())).thenReturn("fake correlation Id");

        assertDoesNotThrow(() -> underTest.onMessage(givenMessage));

        messageUtils.verify(() -> extractBody(any(), any()), times(1));
        messageUtils.verify(() -> extractCorrelationId(any()), times(1));

    }

}