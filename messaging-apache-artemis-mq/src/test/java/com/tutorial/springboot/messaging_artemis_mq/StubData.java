package com.tutorial.springboot.messaging_artemis_mq;

import com.tutorial.springboot.messaging_artemis_mq.model.Acknowledge;
import com.tutorial.springboot.messaging_artemis_mq.model.MessageModel;
import com.tutorial.springboot.messaging_artemis_mq.model.StatusModel;
import jakarta.jms.*;

public class StubData {
    public static final MessageModel NULL_MESSAGE_MODEL = null;
    public static final MessageModel FAKE_MESSAGE_Model = new MessageModel("fake Id", "fake text");
    public static final StatusModel NULL_STATUS_MODEL = null;
    public static final StatusModel FAKE_STATUS_MODEL = new StatusModel(Acknowledge.ACCEPTED, "fake data");
    public static final Message NULL_JMS_MESSAGE = null;

}
