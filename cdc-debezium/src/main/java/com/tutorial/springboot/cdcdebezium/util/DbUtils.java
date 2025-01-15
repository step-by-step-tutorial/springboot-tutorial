package com.tutorial.springboot.cdcdebezium.util;

import com.tutorial.springboot.cdcdebezium.model.JdbcUrlParts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

public final class DbUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(DbUtils.class);

    private DbUtils() {
    }

    public static Optional<JdbcUrlParts> extractJdbcUrlParts(String jdbcUrl) {
        Objects.requireNonNull(jdbcUrl, "jdbcUrl should not be null");
        if (jdbcUrl.isBlank()) {
            LOGGER.error("jdbcUrl should not be blank");
            return Optional.empty();
        }

        if (jdbcUrl.startsWith("jdbc:mysql://")) {
            return splitJdbcUrl(jdbcUrl, Pattern.compile("jdbc:mysql://([^:/]+):([^/]+)/([^?]+)"));
        } else if (jdbcUrl.startsWith("jdbc:postgresql://")) {
            return splitJdbcUrl(jdbcUrl, Pattern.compile("jdbc:postgresql://([^:/]+):([^/]+)/([^?]+)"));
        } else if (jdbcUrl.startsWith("jdbc:oracle:thin:@")) {
            return splitJdbcUrl(jdbcUrl, Pattern.compile("jdbc:oracle:thin:@([^:/]+):(\\d+)/([^?]+)"));
        } else {
            LOGGER.error("Unsupported JDBC URL format: {}", jdbcUrl);
            return Optional.empty();
        }

    }

    private static Optional<JdbcUrlParts> splitJdbcUrl(String jdbcUrl, Pattern pattern) {
        var matcher = pattern.matcher(jdbcUrl);

        if (matcher.find()) {
            var host = matcher.group(1);
            var port = matcher.group(2);
            var dbName = matcher.group(3);
            return Optional.of(new JdbcUrlParts(host, port, dbName));
        } else {
            LOGGER.error("Invalid JDBC URL format");
            return Optional.empty();
        }
    }
}
