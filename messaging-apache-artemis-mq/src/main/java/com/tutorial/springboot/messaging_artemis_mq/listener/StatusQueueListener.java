package com.tutorial.springboot.messaging_artemis_mq.listener;

import com.tutorial.springboot.messaging_artemis_mq.model.StatusModel;
import jakarta.jms.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import static com.tutorial.springboot.messaging_artemis_mq.utils.MessageUtils.*;
import static java.util.Objects.requireNonNull;

@Component
public class StatusQueueListener {

    private final Logger logger = LoggerFactory.getLogger(StatusQueueListener.class);

    @JmsListener(destination = "${destination.status-queue}")
    public void onMessage(final Message message) {
        requireNonNull(message, "message should not be null");
        logger.info("status received from {}", extractDestination(message));
        extractBody(message, StatusModel.class)
                .ifPresentOrElse(
                        body -> logger.info("status processing succeeded: {}", extractCorrelationId(message)),
                        () -> logger.error("status processing failed: {}", extractCorrelationId(message)));
    }

}
