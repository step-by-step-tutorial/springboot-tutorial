package com.tutorial.springboot.messaging_artemis_mq.listener;

import com.tutorial.springboot.messaging_artemis_mq.model.MessageModel;
import com.tutorial.springboot.messaging_artemis_mq.model.StatusModel;
import com.tutorial.springboot.messaging_artemis_mq.service.StatusQueueService;
import jakarta.jms.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import static com.tutorial.springboot.messaging_artemis_mq.model.Acknowledge.ACCEPTED;
import static com.tutorial.springboot.messaging_artemis_mq.model.Acknowledge.FAILED;
import static com.tutorial.springboot.messaging_artemis_mq.utils.MessageUtils.*;
import static java.util.Objects.requireNonNull;

@Component
public class MainQueueListener {

    private final Logger logger = LoggerFactory.getLogger(MainQueueListener.class);

    private final StatusQueueService statusQueueService;

    public MainQueueListener(StatusQueueService statusQueueService) {
        this.statusQueueService = statusQueueService;
    }

    @JmsListener(destination = "${queue.main}")
    public void onMessage(final Message message) {
        requireNonNull(message, "Message should not be null");
        logger.info("Message received from {}", extractDestination(message));
        extractBody(message, MessageModel.class)
                .ifPresentOrElse(
                        body -> {
                            logger.info("Message processing succeeded: {}", body);
                            statusQueueService.push(new StatusModel(ACCEPTED, extractCorrelationId(message), ""));
                        },
                        () -> {
                            logger.info("Message processing failed");
                            statusQueueService.push(new StatusModel(FAILED, extractCorrelationId(message), "Message body could not be extracted"));
                        }
                );
    }

}
