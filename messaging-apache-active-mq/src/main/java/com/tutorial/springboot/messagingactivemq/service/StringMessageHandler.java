package com.tutorial.springboot.messagingactivemq.service;

import com.tutorial.springboot.messagingactivemq.message.AckMessage;
import com.tutorial.springboot.messagingactivemq.message.AckStatus;
import com.tutorial.springboot.messagingactivemq.message.StringMessage;
import jakarta.jms.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import static com.tutorial.springboot.messagingactivemq.utils.MessageUtils.extractBody;
import static com.tutorial.springboot.messagingactivemq.utils.MessageUtils.extractCorrelationId;
import static java.util.Objects.requireNonNull;

@Component
public class StringMessageHandler {

    private final Logger logger = LoggerFactory.getLogger(StringMessageHandler.class);

    @Value("${queue.main-queue}")
    private String mainQueue;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private AckMessagePublisher ackMessagePublisher;

    @JmsListener(destination = "${queue.main-queue}")
    public void onMessage(Message message) {
        requireNonNull(message);
        logger.info("message received from {}", mainQueue);
        extractBody(message, StringMessage.class)
                .ifPresentOrElse(
                        body -> {
                            logger.info("message processing succeeded: {}", body);
                            ackMessagePublisher.sendMessage(new AckMessage(AckStatus.ACCEPTED, body.id()));
                        },
                        () -> ackMessagePublisher.sendMessage(new AckMessage(AckStatus.FAILED, extractCorrelationId(message)))
                );
    }

}
