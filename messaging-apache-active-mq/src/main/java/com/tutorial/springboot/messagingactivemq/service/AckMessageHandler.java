package com.tutorial.springboot.messagingactivemq.service;

import com.tutorial.springboot.messagingactivemq.message.AckMessage;
import jakarta.jms.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import static com.tutorial.springboot.messagingactivemq.utils.MessageUtils.extractBody;
import static java.util.Objects.requireNonNull;

@Component
public class AckMessageHandler {

    private final Logger logger = LoggerFactory.getLogger(AckMessageHandler.class);

    @Value("${queue.ack-queue}")
    private String ackQueue;

    @Autowired
    private JmsTemplate jmsTemplate;

    @JmsListener(destination = "${queue.ack-queue}")
    public void onMessage(Message message) {
        requireNonNull(message);
        logger.info("ack received from {}", ackQueue);
        extractBody(message, AckMessage.class)
                .ifPresentOrElse(
                        body -> logger.info("ack processing succeeded: {}", body),
                        () -> logger.info("ack processing failed"));
    }

}
