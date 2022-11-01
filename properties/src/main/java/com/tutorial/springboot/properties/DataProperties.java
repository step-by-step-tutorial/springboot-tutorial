package com.tutorial.springboot.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "info")
@PropertySource(value = "classpath:data.properties")
public class DataProperties {
  private int id;

  private String name;

  private String dateTime;

  private String[] colors;


  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDateTime() {
    return dateTime;
  }

  public void setDateTime(String dateTime) {
    this.dateTime = dateTime;
  }

  public String[] getColors() {
    return colors;
  }

  public void setColors(String[] colors) {
    this.colors = colors;
  }
}
