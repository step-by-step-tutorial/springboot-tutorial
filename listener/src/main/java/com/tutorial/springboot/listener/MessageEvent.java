package com.tutorial.springboot.listener;

public class MessageEvent {
  private final String text;

  public MessageEvent(String text) {

    this.text = text;
  }

  public String getText() {
    return text;
  }
}
