package com.tutorial.springboot.streaming_kafka;

import com.tutorial.springboot.streaming_kafka.topic.SourceTopicProducer;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class SampleGenerator {

    private static final int MESSAGE_NUMBER = 10;

    private static final int MESSAGE_LENGTH = 10;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public SampleGenerator(SourceTopicProducer sourceTopicProducer) {
        for (int i = 0; i < MESSAGE_NUMBER; i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            sourceTopicProducer.push(generateString(MESSAGE_LENGTH));
        }
    }

    public String generateString(int length) {
        var random = new Random();
        return IntStream.range(0, length)
                .mapToObj(i -> String.valueOf(CHARACTERS.charAt(random.nextInt(CHARACTERS.length()))))
                .collect(Collectors.joining());
    }
}
