package com.tutorial.springboot.rest_basic;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Random;

public final class TestFixtureUtils {
    private TestFixtureUtils() {
    }

    public static <T> T selectByRandom(Collection<T> numbers) {
        @SuppressWarnings("unchecked")
        T[] array = (T[]) Array.newInstance(Object.class, numbers.size());
        return numbers.toArray(array)[new Random().nextInt(numbers.size())];
    }
}
