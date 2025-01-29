package com.tutorial.springboot.artemismq.listener;

import com.tutorial.springboot.artemismq.model.StatusModel;
import jakarta.jms.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import static com.tutorial.springboot.artemismq.utils.MessageUtils.*;
import static java.util.Objects.requireNonNull;

@Component
public class StatusQueueListener {

    private final Logger logger = LoggerFactory.getLogger(StatusQueueListener.class);

    @JmsListener(destination = "${queue.status}")
    public void onMessage(final Message message) {
        requireNonNull(message, "Message should not be null");
        logger.info("Status received from {}", extractDestination(message));
        extractBody(message, StatusModel.class)
                .ifPresentOrElse(
                        body -> logger.info("Status processing succeeded: {}", extractCorrelationId(message)),
                        () -> logger.error("Status processing failed: {}", extractCorrelationId(message)));
    }

}
