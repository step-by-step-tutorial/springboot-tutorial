package com.tutorial.springboot.security_rbac_jwt.util;

import java.lang.reflect.ParameterizedType;

public final class ReflectionUtils {

    private ReflectionUtils() {
    }

    @SuppressWarnings("unchecked")
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
