package com.tutorial.springboot.messagingpulsar;

import com.tutorial.springboot.messagingpulsar.service.MessageService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.tutorial.springboot.messagingpulsar.config.BrokerConstance.TOPIC_NAME;

@SpringBootTest
@ActiveProfiles("test")
class MessageListenerTest {

    private final Logger logger = LoggerFactory.getLogger(MessageListenerTest.class);

    @Autowired
    private MessageService messageService;

    @Test
    void testMessageListenerReceivesMessage() throws InterruptedException {
        var testMessage = "test message";

        logger.info("Sending message to topic: {}", TOPIC_NAME);
        messageService.send(testMessage);

        Thread.sleep(2000);
    }
}
