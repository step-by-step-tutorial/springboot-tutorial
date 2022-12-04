package com.tutorial.springboot.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@ConfigurationProperties(prefix = "info")
@PropertySource(value = "classpath:data.properties")
@EnableConfigurationProperties
public record DataProperties(int id, String name, String dateTime, String[] colors) {
}
