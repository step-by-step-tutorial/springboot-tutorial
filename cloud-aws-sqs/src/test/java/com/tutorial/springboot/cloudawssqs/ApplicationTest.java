package com.tutorial.springboot.cloudawssqs;

import com.tutorial.springboot.cloudawssqs.service.SendMessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTest {

    @Autowired
    private SendMessageService sendMessageService;

    @Test
    void sendAndReceiveMessage() {
        sendMessageService.sendMessage("test message");
    }

}
