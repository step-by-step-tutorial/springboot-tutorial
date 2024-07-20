package com.tutorial.springboot.streaming_kafka.uppercase_transformer;

import com.tutorial.springboot.streaming_kafka.sink.AbstractSink;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SinkImpl extends AbstractSink<Integer, String> {

    public SinkImpl(@Value("${topic.sink}") String topic) {
        super(topic);
    }

}
