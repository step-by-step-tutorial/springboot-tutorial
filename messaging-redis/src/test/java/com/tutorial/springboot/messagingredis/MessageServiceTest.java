package com.tutorial.springboot.messagingredis;

import com.tutorial.springboot.messagingredis.service.MessageService;
import com.tutorial.springboot.messagingredis.storage.MessageStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MessageServiceTest {

    @Autowired
    private MessageService systemUnderTest;

    @Autowired
    private MessageStorage testAssistant;

    @Test
    void givenMessage_whenSendToRedis_thenMessageWillBeReceived() {
        var givenMessage = "test message";

        systemUnderTest.send(givenMessage);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.err.println("error: " + e.getMessage());
        }

        Assertions.assertTrue(testAssistant.count() > 0);
    }

}
