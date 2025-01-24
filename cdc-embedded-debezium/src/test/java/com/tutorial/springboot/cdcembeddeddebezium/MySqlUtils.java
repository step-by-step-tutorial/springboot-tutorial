package com.tutorial.springboot.cdcembeddeddebezium;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public final class MySqlUtils {

    public static final String CONTAINER_NAME = "mysql";

    public static final String USERNAME = "root";

    public static final String PASSWORD = "root";

    public static final String DATABASE_NAME = "tutorial_db";

    public static final String SEQUENCE_NAME = "example_seq";

    public static final String SEQUENCE_COLUMN = "next_val";

    private static final Logger LOGGER = LoggerFactory.getLogger(MySqlUtils.class.getSimpleName());

    private MySqlUtils() {
    }

    public static int runMysqlCommand(String containerName, String username, String password, String... query) {
        try {
            var command = String.format("docker exec %s mysql -u %s -p%s -h localhost -e \"%s\"", containerName, username, password, String.join(" ", query));
            var process = new ProcessBuilder(command.split(" "))
                    .redirectErrorStream(true)
                    .start();

            int exitCode = process.waitFor();
            LOGGER.info("exit code (The command): {}", exitCode);

            return exitCode;
        } catch (Exception e) {
            LOGGER.error("The command failed due to: {}", e.getMessage());
            return -1;
        }
    }

    public static long runMysqlNextId(String containerName, String username, String password, String databaseName, String sequenceName) {

        var currentId = currentId(containerName, username, password, databaseName, sequenceName);
        var nextId = incrementId(containerName, username, password, databaseName, sequenceName, currentId);

        LOGGER.info("Current ID: {}, Incremented to ID: {}", currentId, nextId);

        return currentId;
    }

    private static long currentId(String containerName, String username, String password, String databaseName, String sequenceName) {
        try {
            var currentId = 0L;
            var query = new String[]{
                    String.format("USE %s;", databaseName),
                    String.format("SELECT %s FROM %s;", SEQUENCE_COLUMN, sequenceName)
            };

            var command = String.format("docker exec %s mysql -u %s -p%s -h localhost -e \"%s\"", containerName, username, password, String.join(" ", query));
            var process = new ProcessBuilder(command.split(" "))
                    .redirectErrorStream(true)
                    .start();

            try (var reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                var rows = reader.lines().map(String::trim).toList();
                currentId = Long.parseLong(rows.getLast());
            }

            int exitCode = process.waitFor();
            LOGGER.info("exit code of command [Get current ID]: {}", exitCode);

            return currentId;
        } catch (Exception e) {
            LOGGER.error("The command [Get current ID] failed due to: {}", e.getMessage());
            return -1;
        }
    }

    private static long incrementId(String containerName, String username, String password, String databaseName, String sequenceName, long currentId) {
        try {
            var nextId = currentId + 1;
            var query = new String[]{
                    String.format("USE %s;", databaseName),
                    String.format("INSERT INTO %s (%s) VALUES (%d);", sequenceName, SEQUENCE_COLUMN, nextId)
            };

            var command = String.format("docker exec %s mysql -u %s -p%s -h localhost -e \"%s\"", containerName, username, password, String.join(" ", query));
            var process = new ProcessBuilder(command.split(" "))
                    .redirectErrorStream(true)
                    .start();

            int exitCode = process.waitFor();
            LOGGER.info("exit code of command [Increment ID]: {}", exitCode);

            return nextId;
        } catch (Exception e) {
            LOGGER.error("The command [Increment ID] failed due to: {}", e.getMessage());
            return -1;
        }
    }

    public static void waitForChangeDataCapture() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            LOGGER.error("Current thread failed due to: {}", e.getMessage());
        }
    }
}
