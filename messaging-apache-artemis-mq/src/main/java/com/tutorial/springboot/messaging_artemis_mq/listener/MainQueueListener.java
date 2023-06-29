package com.tutorial.springboot.messaging_artemis_mq.listener;

import com.tutorial.springboot.messaging_artemis_mq.model.Acknowledge;
import com.tutorial.springboot.messaging_artemis_mq.model.MessageModel;
import com.tutorial.springboot.messaging_artemis_mq.model.StatusModel;
import com.tutorial.springboot.messaging_artemis_mq.service.StatusQueueClient;
import jakarta.jms.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import static com.tutorial.springboot.messaging_artemis_mq.utils.MessageUtils.*;
import static java.util.Objects.requireNonNull;

@Component
public class MainQueueListener {

    private final Logger logger = LoggerFactory.getLogger(MainQueueListener.class);

    private final StatusQueueClient statusQueueClient;

    public MainQueueListener(StatusQueueClient statusQueueClient) {
        this.statusQueueClient = statusQueueClient;
    }

    @JmsListener(destination = "${destination.main-queue}")
    public void onMessage(final Message message) {
        requireNonNull(message, "message should not be null");
        logger.info("message received from {}", extractDestination(message));
        extractBody(message, MessageModel.class)
                .ifPresentOrElse(
                        body -> {
                            logger.info("message processing succeeded: {}", body);
                            statusQueueClient.push(new StatusModel(Acknowledge.ACCEPTED, extractCorrelationId(message)));
                        },
                        () -> {
                            logger.info("message processing failed");
                            statusQueueClient.push(new StatusModel(Acknowledge.FAILED, extractCorrelationId(message)));
                        }
                );
    }

}
