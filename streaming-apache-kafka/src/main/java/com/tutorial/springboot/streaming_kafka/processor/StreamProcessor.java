package com.tutorial.springboot.streaming_kafka.processor;

import org.apache.kafka.streams.kstream.KStream;

import java.io.Serializable;

public interface StreamProcessor<IK extends Serializable, IV extends Serializable, OK extends Serializable, OV extends Serializable> {
    KStream<OK, OV> process(KStream<IK, IV> stream);
}
