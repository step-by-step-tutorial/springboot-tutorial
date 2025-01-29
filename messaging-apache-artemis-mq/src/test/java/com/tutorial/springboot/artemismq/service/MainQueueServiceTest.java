package com.tutorial.springboot.artemismq.service;

import com.tutorial.springboot.artemismq.model.MessageModel;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles({"test"})
class MainQueueServiceTest {

    @InjectMocks
    MainQueueService systemUnderTest;

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
        final MessageModel givenModel = null;

        final var expectedException = NullPointerException.class;
        final var expectedExceptionMessage = "MessageModel should not be null";

        final var actual = assertThrows(expectedException, () -> systemUnderTest.push(givenModel));

        assertNotNull(actual);
        assertEquals(expectedExceptionMessage, actual.getMessage());
        verify(jmsTemplate, times(0)).send(anyString(), any());
    }

    @Test
    void messageShouldBePushedToTheQueue() {
        final var givenModel = new MessageModel("fake Id", "fake text");

        final var fakeMessageCreator = mock(MessageCreator.class);
        final var fakeDestination = "fake queue";
        messageUtils.when(() -> createSerializableMessage(any())).thenReturn(fakeMessageCreator);
        doNothing()
                .when(jmsTemplate)
                .send(fakeDestination, fakeMessageCreator);

        assertDoesNotThrow(() -> systemUnderTest.push(givenModel));

        messageUtils.verify(() -> createSerializableMessage(any()), times(1));
        verify(jmsTemplate, times(1)).send(fakeDestination, fakeMessageCreator);
    }

}
