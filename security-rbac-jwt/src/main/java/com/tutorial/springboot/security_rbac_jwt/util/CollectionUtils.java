package com.tutorial.springboot.security_rbac_jwt.util;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public final class CollectionUtils {

    public static final int BATCH_SIZE = 10;

    private CollectionUtils() {
    }

    public static int calculateBatchNumber(int length) {
        return (int) Math.ceil((double) length / BATCH_SIZE);
    }

    public static <T> Stream<T> selectBatch(List<T> list, int batchIndex, int batchSize) {
        return list.stream()
                .skip((long) batchIndex * batchSize)
                .limit(batchSize);
    }

    public static <T, COMPARATOR_TYPE> Stream<T> removeDuplication(Collection<T> collection, Function<T, COMPARATOR_TYPE> comparator) {
        return collection
                .stream()
                .collect(collectingAndThen(toMap(comparator, e -> e, (existing, replacement) -> existing), map -> map.values().stream()));
    }

    public static <T> TripleCollection<T> compareCollections(Collection<T> ownerList, Collection<T> newList) {
        var addedItems = newList.stream()
                .filter(item -> !ownerList.contains(item))
                .collect(toList());

        var removedItems = ownerList.stream()
                .filter(item -> !newList.contains(item))
                .collect(toList());

        var commonItems = newList.stream()
                .filter(ownerList::contains)
                .collect(toList());

        return new TripleCollection<>(addedItems, removedItems, commonItems);
    }
}
