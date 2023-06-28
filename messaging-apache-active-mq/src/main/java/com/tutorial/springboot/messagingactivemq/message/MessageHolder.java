package com.tutorial.springboot.messagingactivemq.message;

import java.io.Serializable;

public record MessageHolder(String id, String text) implements Serializable {

}
