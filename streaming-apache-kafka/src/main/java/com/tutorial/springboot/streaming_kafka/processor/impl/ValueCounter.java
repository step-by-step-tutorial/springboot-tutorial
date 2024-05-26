//package com.tutorial.springboot.streaming_kafka.processor.impl;
//
//import com.tutorial.springboot.streaming_kafka.processor.StreamProcessor;
//import org.apache.kafka.streams.kstream.KStream;
//import org.springframework.stereotype.Component;
//
//@Component
//public class ValueCounter implements StreamProcessor<String, String, String, Long> {
//
//    public ValueCounter() {
//    }
//
//    @Override
//    public KStream<String, Long> process(KStream<String, String> stream) {
//        return stream.groupBy((k, v) -> v).count().toStream();
//    }
//}
