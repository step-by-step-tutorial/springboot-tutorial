package com.tutorial.springboot.streaming_kafka;

import com.tutorial.springboot.streaming_kafka.topic.SourceTopicProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles({"test"})
@DisplayName("unit tests of kafka main topic service")
class SourceTopicProducerTest {

    @InjectMocks
    SourceTopicProducer systemUnderTest;

    @Mock
    KafkaTemplate<String, String> kafkaTemplate;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(systemUnderTest, "topic", "fake topic");
    }

    @Test
    @DisplayName("should throw NullPointerException when given message is null")
    void shouldThrowNullPointerExceptionWhenMessageIsNull() {
        final String givenMessage = null;

        final var expectedException = NullPointerException.class;
        final var expectedExceptionMessage = "message should not be null";

        final var actual = assertThrows(expectedException, () -> systemUnderTest.push(givenMessage));

        assertNotNull(actual);
        assertEquals(expectedExceptionMessage, actual.getMessage());
        verify(kafkaTemplate, times(0)).send(anyString(), any());
    }

    @Test
    @DisplayName("the message should be pushed to the queue")
    void messageShouldBePushedToTheQueue() {
        final var givenMessage = "fake message";

        final var captor = ArgumentCaptor.forClass(Message.class);
        doReturn(new CompletableFuture<>()).when(kafkaTemplate).send(captor.capture());

        assertDoesNotThrow(() -> systemUnderTest.push(givenMessage));

        verify(kafkaTemplate, times(1)).send(captor.capture());
        assertThat(captor.getValue()).satisfies(msg -> {
            assertNotNull(msg.getPayload());
            assertNotNull(msg.getHeaders().get(KafkaHeaders.CORRELATION_ID));
            assertNotNull(msg.getHeaders().get(KafkaHeaders.TOPIC));
        });
    }

}
