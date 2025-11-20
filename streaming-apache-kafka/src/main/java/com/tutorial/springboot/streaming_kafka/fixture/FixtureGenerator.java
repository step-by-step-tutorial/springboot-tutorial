package com.tutorial.springboot.streaming_kafka.fixture;

import com.tutorial.springboot.streaming_kafka.service.SourceTopicService;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;

@Component
public class FixtureGenerator {

    private static final int MESSAGE_NUMBER = 10;

    private static final int MESSAGE_LENGTH = 10;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public FixtureGenerator(SourceTopicService sourceTopicService) {
        for (int i = 0; i < MESSAGE_NUMBER; i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            sourceTopicService.push(generateString(MESSAGE_LENGTH));
        }
    }

    public String generateString(int length) {
        var random = new Random();
        return IntStream.range(0, length)
                .mapToObj(i -> String.valueOf(CHARACTERS.charAt(random.nextInt(CHARACTERS.length()))))
                .collect(joining());
    }
}
