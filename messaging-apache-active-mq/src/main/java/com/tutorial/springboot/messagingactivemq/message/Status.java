package com.tutorial.springboot.messagingactivemq.message;

import java.io.Serializable;

public record Status(Acknowledge status, String additionalData) implements Serializable {

}
