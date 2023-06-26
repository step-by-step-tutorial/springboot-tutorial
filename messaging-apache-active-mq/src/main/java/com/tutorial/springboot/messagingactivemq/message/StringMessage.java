package com.tutorial.springboot.messagingactivemq.message;

import java.io.Serializable;

public record StringMessage(String id, String text) implements Serializable {

}
