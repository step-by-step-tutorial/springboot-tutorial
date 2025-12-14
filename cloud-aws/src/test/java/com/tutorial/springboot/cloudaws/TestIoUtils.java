package com.tutorial.springboot.cloudaws;

import com.tutorial.springboot.cloudaws.util.IoUtils;

import java.io.InputStream;

public final class TestIoUtils {

    private TestIoUtils() {
    }

    public static InputStream createTestInputstream() {
        return IoUtils.createStream("test file content".getBytes());
    }
}
