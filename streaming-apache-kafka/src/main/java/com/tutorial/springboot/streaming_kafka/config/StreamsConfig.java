package com.tutorial.springboot.streaming_kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.support.serializer.JsonSerde;

@Configuration(proxyBeanMethods = false)
@EnableKafkaStreams
public class StreamsConfig {

    @Value("${topic.source}")
    private String source;

    @Value("${topic.sink}")
    private String sink;

    @Bean
    public NewTopic sourceTopic() {
        return TopicBuilder.name(source)
                .partitions(10)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic sinkTopic() {
        return TopicBuilder.name(sink)
                .partitions(5)
                .replicas(1)
                .build();
    }

}
