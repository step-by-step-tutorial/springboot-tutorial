package com.tutorial.springboot.messagingactivemq.service;

import com.tutorial.springboot.messagingactivemq.message.StringMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import static com.tutorial.springboot.messagingactivemq.utils.MessageUtils.createSerializableMessage;
import static java.util.Objects.requireNonNull;

@Component
public class StringMessagePublisher {

    private final Logger logger = LoggerFactory.getLogger(StringMessagePublisher.class);

    @Value("${queue.main-queue}")
    private String mainQueue;

    @Autowired
    private JmsTemplate jmsTemplate;

    public void publish(StringMessage message) {
        requireNonNull(message);
        jmsTemplate.send(mainQueue, createSerializableMessage(message));
        logger.info("message sent to {}: {}", mainQueue, message);
    }

}
