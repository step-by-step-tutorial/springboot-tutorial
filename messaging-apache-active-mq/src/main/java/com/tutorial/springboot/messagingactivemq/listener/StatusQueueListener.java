package com.tutorial.springboot.messagingactivemq.listener;

import com.tutorial.springboot.messagingactivemq.message.Status;
import jakarta.jms.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import static com.tutorial.springboot.messagingactivemq.utils.MessageUtils.*;
import static java.util.Objects.requireNonNull;

@Component
public class StatusQueueListener {

    private final Logger logger = LoggerFactory.getLogger(StatusQueueListener.class);

    @JmsListener(destination = "${queue.status-queue}")
    public void onMessage(Message message) {
        requireNonNull(message, "message should not be null");
        logger.info("status received from {}", extractDestination(message));
        extractBody(message, Status.class)
                .ifPresentOrElse(
                        body -> logger.info("status {} processing succeeded: {}", extractCorrelationId(message), body),
                        () -> logger.error("status {} processing failed", extractCorrelationId(message)));
    }

}
