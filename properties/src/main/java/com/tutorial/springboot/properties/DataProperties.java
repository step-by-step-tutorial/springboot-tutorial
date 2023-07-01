package com.tutorial.springboot.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@EnableConfigurationProperties
@PropertySource(value = "classpath:data.properties")
@ConfigurationProperties(prefix = "info")
public record DataProperties(int id, String name, String dateTime, String[] colors) {
}
