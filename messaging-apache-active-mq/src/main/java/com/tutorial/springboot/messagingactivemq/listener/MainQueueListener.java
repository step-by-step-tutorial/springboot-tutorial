package com.tutorial.springboot.messagingactivemq.listener;

import com.tutorial.springboot.messagingactivemq.message.Acknowledge;
import com.tutorial.springboot.messagingactivemq.message.MessageHolder;
import com.tutorial.springboot.messagingactivemq.message.Status;
import com.tutorial.springboot.messagingactivemq.service.StatusQueueClient;
import com.tutorial.springboot.messagingactivemq.utils.MessageUtils;
import jakarta.jms.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import static com.tutorial.springboot.messagingactivemq.utils.MessageUtils.extractBody;
import static com.tutorial.springboot.messagingactivemq.utils.MessageUtils.extractCorrelationId;
import static java.util.Objects.requireNonNull;

@Component
public class MainQueueListener {

    private final Logger logger = LoggerFactory.getLogger(MainQueueListener.class);

    private final StatusQueueClient statusQueueClient;

    public MainQueueListener(StatusQueueClient statusQueueClient) {
        this.statusQueueClient = statusQueueClient;
    }

    @JmsListener(destination = "${queue.main-queue}")
    public void onMessage(Message message) {
        requireNonNull(message, "message should not be null");
        logger.info("message received from {}", MessageUtils.extractDestination(message));
        extractBody(message, MessageHolder.class)
                .ifPresentOrElse(
                        body -> {
                            logger.info("message processing succeeded: {}", body);
                            statusQueueClient.push(new Status(Acknowledge.ACCEPTED, extractCorrelationId(message)));
                        },
                        () -> statusQueueClient.push(new Status(Acknowledge.FAILED, extractCorrelationId(message)))
                );
    }

}
