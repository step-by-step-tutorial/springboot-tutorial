package com.tutorial.springboot.messaging_artemis_mq.model;

import java.io.Serializable;

public record StatusModel(Acknowledge status, String messageId, String description) implements Serializable {

}
