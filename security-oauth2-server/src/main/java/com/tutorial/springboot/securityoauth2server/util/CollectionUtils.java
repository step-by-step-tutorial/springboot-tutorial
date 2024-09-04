package com.tutorial.springboot.securityoauth2server.util;

import java.util.List;
import java.util.stream.Stream;

public final class CollectionUtils {

    public static final int BATCH_SIZE = 10;

    private CollectionUtils() {
    }

    public static int calculateBatchNumber(int length) {
        return (int) Math.ceil((double) length / BATCH_SIZE);
    }

    public static <T> Stream<T> selectBatch(List<T> list, int batchIndex) {
        return list.stream()
                .skip((long) batchIndex * BATCH_SIZE)
                .limit(BATCH_SIZE);
    }

}
