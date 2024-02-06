package com.tutorial.springboot.rest_basic;

import com.tutorial.springboot.rest_basic.dto.SampleDto;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Random;
import java.util.stream.IntStream;

public class TestFixture {

    public static final Random randomCodeGenerator = new Random();
    public static final Random randomSizeGenerator = new Random();

    private TestFixture() {
    }

    public static SampleDto oneSample() {
        return SampleDto.builder()
                .text("fake")
                .code(1)
                .datetime(LocalDateTime.now())
                .build();
    }

    public static SampleDto[] multiSample() {
        return IntStream.range(1, randomSizeGenerator.nextInt(2, 5))
                .boxed()
                .map(integer -> SampleDto.builder()
                        .text(String.format("fake %s", integer))
                        .code(randomCodeGenerator.nextInt())
                        .datetime(LocalDateTime.now())
                        .build())
                .toArray(SampleDto[]::new);
    }

    public static <T> T selectByRandom(Collection<T> numbers, Class<T> type) {
        @SuppressWarnings("unchecked")
        T[] array = (T[]) Array.newInstance(type, numbers.size());
        return numbers.toArray(array)[new Random().nextInt(numbers.size())];
    }
}