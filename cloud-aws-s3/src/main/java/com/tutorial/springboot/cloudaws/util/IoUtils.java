package com.tutorial.springboot.cloudaws.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public final class IoUtils {

    private IoUtils() {
    }

    public static InputStream createStream(byte[] content) {
        return new ByteArrayInputStream(content);
    }
}
