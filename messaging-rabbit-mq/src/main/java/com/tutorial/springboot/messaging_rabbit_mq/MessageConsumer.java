package com.tutorial.springboot.messaging_rabbit_mq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    @RabbitListener(queues = "myQueue")
    public void processMessage(String message) {
        System.out.println("Message received: " + message);
    }
}
