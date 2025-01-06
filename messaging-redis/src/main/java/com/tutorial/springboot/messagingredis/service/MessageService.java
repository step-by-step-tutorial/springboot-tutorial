package com.tutorial.springboot.messagingredis.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.Topic;
import org.springframework.stereotype.Service;

import static java.util.Objects.requireNonNull;

@Service
public class MessageService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RedisTemplate<String, String> redisTemplate;

    private final Topic topic;

    public MessageService(RedisTemplate<String, String> redisTemplate, Topic topic) {
        this.redisTemplate = redisTemplate;
        this.topic = topic;
    }

    public void send(String message) {
        requireNonNull(message, "Message should not be null.");
        if (message.isBlank()) {
            throw new IllegalArgumentException("Message should not be empty.");
        }
        redisTemplate.convertAndSend(topic.getTopic(), message);
        logger.info("Message sent: {}", message);
    }
}
