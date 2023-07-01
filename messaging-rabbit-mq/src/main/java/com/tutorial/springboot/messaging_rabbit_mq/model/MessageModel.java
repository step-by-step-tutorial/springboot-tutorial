package com.tutorial.springboot.messaging_rabbit_mq.model;

import java.io.Serializable;

public record MessageModel(String id, String text) implements Serializable {

}
