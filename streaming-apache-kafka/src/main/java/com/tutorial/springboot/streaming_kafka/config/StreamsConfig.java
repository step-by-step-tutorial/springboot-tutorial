package com.tutorial.springboot.streaming_kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.TopicBuilder;

@Configuration(proxyBeanMethods = false)
@EnableKafkaStreams
public class StreamsConfig {

    @Bean
    public NewTopic sourceTopic(@Value("${topic.source}") String source) {
        return TopicBuilder.name(source)
                .partitions(10)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic sinkTopic(@Value("${topic.sink}") String sink) {
        return TopicBuilder.name(sink)
                .partitions(5)
                .replicas(1)
                .build();
    }

}
