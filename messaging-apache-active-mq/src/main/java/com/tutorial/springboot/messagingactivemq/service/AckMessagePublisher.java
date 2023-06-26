package com.tutorial.springboot.messagingactivemq.service;

import com.tutorial.springboot.messagingactivemq.message.AckMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import static com.tutorial.springboot.messagingactivemq.utils.MessageUtils.createSerializableMessage;
import static java.util.Objects.requireNonNull;

@Component
public class AckMessagePublisher {

    private final Logger logger = LoggerFactory.getLogger(AckMessagePublisher.class);

    @Value("${queue.ack-queue}")
    private String ackQueue;

    @Autowired
    private JmsTemplate jmsTemplate;

    public void publish(AckMessage message) {
        requireNonNull(message);
        jmsTemplate.send(ackQueue, createSerializableMessage(message));
        logger.info("ack sent to {}: {}", ackQueue, message);
    }
}
