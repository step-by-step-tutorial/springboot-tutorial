package com.tutorial.springboot.cdcdebezium.config;

import com.tutorial.springboot.cdcdebezium.util.DbUtils;
import io.debezium.config.Configuration;
import io.debezium.embedded.Connect;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.ChangeEventFormat;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Profile("embedded-debezium")
public class EmbeddedDebeziumEngine {

    private final Logger logger = LoggerFactory.getLogger(EmbeddedDebeziumEngine.class);

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${embedded-debezium.topic-prefix}")
    private String topicPrefix;

    @Value("${embedded-debezium.tables}")
    private String tables;

    @Value("${embedded-debezium.kafka-url}")
    private String kafkaUrl;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @PostConstruct
    private void startEngine() {
        var jdbcUrl = DbUtils.toJdbcUrlModel(url);
        if (jdbcUrl.isEmpty()) {
            throw new IllegalArgumentException("Invalid JDBC URL: " + url);
        }

        var url = jdbcUrl.get();

        var dbhistoryFile = new File(System.getProperty("java.io.tmpdir"), "dbhistory.dat");
        var offsetsFile = new File(System.getProperty("java.io.tmpdir"), "offsets.dat");
        var config = Configuration.create()
                .with("name", "embedded-engine")
                .with("connector.class", "io.debezium.connector.mysql.MySqlConnector")
                .with("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore")
                .with("offset.storage.file.filename", offsetsFile.getAbsolutePath())
                .with("offset.flush.interval.ms", "60000")
                .with("tasks.max", "1")
                .with("database.server.id", "1")
                .with("database.hostname", url.host())
                .with("database.port", url.port())
                .with("database.user", dbUser)
                .with("database.password", dbPassword)
                .with("database.whitelist", url.dbName())
                .with("table.include.list", tables)
                .with("topic.prefix", topicPrefix)
                .with("schema.history.internal.kafka.bootstrap.servers", kafkaUrl)
                .with("schema.history.internal.kafka.topic", "schema-changes.db")
                .with("database.history", "io.debezium.relational.history.FileDatabaseHistory")
                .with("database.history.file.filename",dbhistoryFile.getAbsolutePath())
                .build();

        var engine = DebeziumEngine.create(ChangeEventFormat.of(Connect.class))
                .using(config.asProperties())
                .notifying(event -> logger.info("Received event: {}", event.record().value()))
                .build();

        executor.submit(engine);
    }

    @PreDestroy
    private void stopEngine() {
        executor.shutdown();
    }
}
