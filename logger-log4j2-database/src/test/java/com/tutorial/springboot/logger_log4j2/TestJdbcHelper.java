package com.tutorial.springboot.logger_log4j2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Optional;

@Component
@ActiveProfiles("test")
public class TestJdbcHelper {

    @Value("${mysql.host}")
    private String host;

    @Value("${mysql.port}")
    private int port;

    @Value("${mysql.database}")
    private String database;

    @Value("${mysql.user}")
    private String user;

    @Value("${mysql.password}")
    private String password;

    public <E> Optional<E> executeQuery(Throwable<Statement, E> query) {
        try (var connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%s/%s", host, port, database), user, password); var statement = connection.createStatement()) {
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