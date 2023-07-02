package com.tutorial.springboot.messaging_rabbit_mq;

import com.tutorial.springboot.messaging_rabbit_mq.model.MessageModel;
import com.tutorial.springboot.messaging_rabbit_mq.service.MainQueueService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@ActiveProfiles({"unit tests of main queue service"})
class MainQueueServiceTest {

    @Autowired
    private MainQueueService systemUnderTest;

    @Test
    @DisplayName("the message should be pushed to the queue")
    void messageShouldBePushedToTheQueue() {
        var givenModel = new MessageModel(UUID.randomUUID().toString(), "test text");

        assertDoesNotThrow(() -> systemUnderTest.sendMessage(givenModel));
    }

}
