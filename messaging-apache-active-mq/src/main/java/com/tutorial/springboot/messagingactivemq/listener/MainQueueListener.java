package com.tutorial.springboot.messagingactivemq.listener;

import com.tutorial.springboot.messagingactivemq.message.Acknowledge;
import com.tutorial.springboot.messagingactivemq.message.MessageHolder;
import com.tutorial.springboot.messagingactivemq.message.Status;
import com.tutorial.springboot.messagingactivemq.service.StatusQueueClient;
import jakarta.jms.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import static com.tutorial.springboot.messagingactivemq.utils.MessageUtils.extractBody;
import static com.tutorial.springboot.messagingactivemq.utils.MessageUtils.extractCorrelationId;
import static java.util.Objects.requireNonNull;

@Component
public class MainQueueListener {

    private final Logger logger = LoggerFactory.getLogger(MainQueueListener.class);

    private final String destination;

    private final StatusQueueClient statusQueueClient;

    public MainQueueListener(
            @Value("${queue.main-queue}")
            String destination,
            StatusQueueClient statusQueueClient) {
        this.destination = destination;
        this.statusQueueClient = statusQueueClient;
    }

    @JmsListener(destination = "${queue.main-queue}")
    public void onMessage(Message message) {
        requireNonNull(message);
        logger.info("message received from {}", destination);
        extractBody(message, MessageHolder.class)
                .ifPresentOrElse(
                        body -> {
                            logger.info("message processing succeeded: {}", body);
                            statusQueueClient.publish(new Status(Acknowledge.ACCEPTED, body.id()));
                        },
                        () -> statusQueueClient.publish(new Status(Acknowledge.FAILED, extractCorrelationId(message)))
                );
    }

}
