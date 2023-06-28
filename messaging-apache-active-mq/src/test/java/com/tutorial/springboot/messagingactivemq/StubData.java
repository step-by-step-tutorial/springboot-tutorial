package com.tutorial.springboot.messagingactivemq;

import com.tutorial.springboot.messagingactivemq.message.Acknowledge;
import com.tutorial.springboot.messagingactivemq.message.MessageHolder;
import com.tutorial.springboot.messagingactivemq.message.Status;
import jakarta.jms.*;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import java.io.Serializable;

public class StubData {
    public static final MessageHolder NULL_MESSAGE = null;
    public static final MessageHolder FAKE_MESSAGE = new MessageHolder("fake Id", "fake text");
    public static final Status NULL_STATUS = null;
    public static final Status FAKE_STATUS = new Status(Acknowledge.ACCEPTED, "fake data");
    public static final Message NULL_JMS_MESSAGE = null;

}
