package com.tutorial.springboot.artemismq.model;

import java.io.Serializable;

public record StatusModel(Acknowledge status, String messageId, String description) implements Serializable {

}
