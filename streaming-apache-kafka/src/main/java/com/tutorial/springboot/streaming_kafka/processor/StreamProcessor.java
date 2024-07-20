package com.tutorial.springboot.streaming_kafka.processor;

import org.apache.kafka.streams.kstream.KStream;

public interface StreamProcessor<IK, IV, OK, OV> {
    KStream<OK, OV> process(KStream<IK, IV> stream);
}
