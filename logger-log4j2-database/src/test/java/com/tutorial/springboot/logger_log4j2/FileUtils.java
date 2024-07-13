package com.tutorial.springboot.logger_log4j2;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.util.Objects.requireNonNull;

public final class FileUtils {

    private FileUtils() {
    }

    public static String readFileContent(String fileName) throws URISyntaxException, IOException {
        var schema = requireNonNull(ApplicationTest.class.getClassLoader().getResource(fileName)).toURI();
        var content = Files.readString(Paths.get(schema));
        return content;
    }
}