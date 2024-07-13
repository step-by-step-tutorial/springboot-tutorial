package com.tutorial.springboot.logger_log4j2;

import org.testcontainers.containers.JdbcDatabaseContainer;

import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Optional;

public final class JdbcUtils {

    private JdbcUtils() {
    }

    public static <E> Optional<E> executeQuery(JdbcDatabaseContainer<?> db, Throwable<Statement, E> query) {
        try (var connection = DriverManager.getConnection(db.getJdbcUrl(), db.getUsername(), db.getPassword());
             var statement = connection.createStatement()) {
            return Optional.of(query.accept(statement));
        } catch (Exception e) {
            System.err.println(String.format("Create mysql connection failed due to: %s", e.getMessage()));
            return Optional.empty();
        }
    }

    @FunctionalInterface
    public interface Throwable<T, E> {
        E accept(T t) throws Exception;

    }
}