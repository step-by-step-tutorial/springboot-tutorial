package com.tutorial.springboot.artemismq.model;

import java.io.Serializable;

public record MessageModel(String id, String text) implements Serializable {

}
