package com.tutorial.springboot.security_rbac_jwt.util;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toMap;

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

    public static <T, COMPARATOR_TYPE> Stream<T> removeDuplication(Collection<T> collection, Function<T, COMPARATOR_TYPE> comparator) {
        return collection
                .stream()
                .collect(collectingAndThen(toMap(comparator, e -> e, (existing, replacement) -> existing), map -> map.values().stream()));
    }
}
