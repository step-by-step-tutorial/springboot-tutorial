package com.tutorial.springboot.messagingactivemq.service;

import com.tutorial.springboot.messagingactivemq.message.MessageHolder;

public class StubData {
        static final MessageHolder NULL_MESSAGE = null;
        static final MessageHolder FAKE_MESSAGE = new MessageHolder("fake Id", "fake text");
    }