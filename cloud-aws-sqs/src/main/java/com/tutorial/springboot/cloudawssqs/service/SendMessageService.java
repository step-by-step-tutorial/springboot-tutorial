package com.tutorial.springboot.cloudawssqs.service;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;

import java.util.UUID;

@Service
public class SendMessageService {

    private final Logger logger = LoggerFactory.getLogger(SendMessageService.class);

    private final SqsTemplate sqsTemplate;

    private final String queueName;

    public SendMessageService(SqsTemplate sqsTemplate, @Value("${app.queue.name}") String queueName) {
        this.sqsTemplate = sqsTemplate;
        this.queueName = queueName;
    }

    public void sendMessage(String message) {
        var correlationId = UUID.randomUUID().toString();
        sqsTemplate.send(to -> to.queue(queueName)
                .header("CorrelationId", correlationId)
                .payload(message));
        logger.info("Message sent to: Queue={}, Payload={}, Correlation ID={}", queueName, message, correlationId);
    }

}
