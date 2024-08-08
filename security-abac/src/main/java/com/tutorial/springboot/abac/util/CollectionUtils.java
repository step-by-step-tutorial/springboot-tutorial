package com.tutorial.springboot.abac.util;

import java.util.stream.Stream;

public final class CollectionUtils {

    public static final int BATCH_SIZE = 10;

    private CollectionUtils() {
    }

    public static int calculateNumberOfBatch(int length) {
        return (int) Math.ceil((double) length / BATCH_SIZE);
    }

    public static <T> Stream<T> selectBatch(T[] array, int batchIndex) {
        return Stream.of(array)
                .skip((long) batchIndex * BATCH_SIZE)
                .limit(BATCH_SIZE);
    }

}
