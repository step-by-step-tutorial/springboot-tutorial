package com.tutorial.springboot.messagingredis.service;

import com.tutorial.springboot.messagingredis.storage.MessageStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class MessageListenerImpl implements MessageListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MessageStorage messageStorage;


    public void onMessage(Message message, byte[] pattern) {
        var content = message.toString();
        logger.info("Message received: {}", content);
        messageStorage.insert(content);
    }
}