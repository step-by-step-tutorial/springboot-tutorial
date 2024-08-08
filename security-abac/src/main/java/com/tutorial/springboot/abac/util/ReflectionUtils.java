package com.tutorial.springboot.abac.util;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.stream.Stream;

public final class ReflectionUtils {

    private ReflectionUtils() {
    }

    public static <T> Class<T> identifyType(int index, Class<?> clzz) {
        try {
            Type superClass = clzz.getGenericSuperclass();
            Type entityArgument = ((ParameterizedType) superClass).getActualTypeArguments()[index];
            return (Class<T>) Class.forName(entityArgument.getTypeName());
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static <T> T[] arrayFrom(Class<T> clazz, int size) {
        return (T[]) Array.newInstance(clazz, size);
    }

    public static <T> T[] arrayFrom(Stream<T> stream, Class<T> clazz) {
        return stream.toArray(size -> arrayFrom(clazz, size));
    }
}
