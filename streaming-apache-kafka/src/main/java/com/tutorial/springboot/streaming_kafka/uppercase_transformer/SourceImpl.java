package com.tutorial.springboot.streaming_kafka.uppercase_transformer;

import com.tutorial.springboot.streaming_kafka.source.AbstractSource;
import org.apache.kafka.streams.StreamsBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SourceImpl extends AbstractSource<Integer, String> {

    public SourceImpl(@Value("${topic.source}") String source, StreamsBuilder builder) {
        super(source, builder);
    }

}
