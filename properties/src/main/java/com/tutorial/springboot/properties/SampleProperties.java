package com.tutorial.springboot.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = "classpath:sample.properties")
@ConfigurationProperties(prefix = "info")
@EnableConfigurationProperties
public record SampleProperties(int id, String name, String dateTime, String[] colors) {
}
