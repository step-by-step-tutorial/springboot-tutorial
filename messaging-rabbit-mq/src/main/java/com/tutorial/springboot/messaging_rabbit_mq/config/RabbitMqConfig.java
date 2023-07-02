package com.tutorial.springboot.messaging_rabbit_mq.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMqConfig {
    @Bean
    public Queue queue(@Value("${destination.main-queue}") final String name) {
        return new Queue(name, false);
    }
}
