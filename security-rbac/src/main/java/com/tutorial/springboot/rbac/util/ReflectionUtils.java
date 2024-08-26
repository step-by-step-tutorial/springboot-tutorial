package com.tutorial.springboot.rbac.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@SuppressWarnings("unchecked")
public final class ReflectionUtils {

    private ReflectionUtils() {
    }

    public static <T> Class<T> identifyType(int index, Class<?> clazz) {
        try {
            var superClass = clazz.getGenericSuperclass();
            var entityArgument = ((ParameterizedType) superClass).getActualTypeArguments()[index];
            return (Class<T>) Class.forName(entityArgument.getTypeName());
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

}
