package com.tutorial.springboot.messagingactivemq.message;

import java.io.Serializable;

public record AckMessage(AckStatus status, String additionalData) implements Serializable {

}
