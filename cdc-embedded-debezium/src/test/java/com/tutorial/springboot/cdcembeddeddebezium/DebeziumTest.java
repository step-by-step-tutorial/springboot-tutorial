package com.tutorial.springboot.cdcembeddeddebezium;

import net.datafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.tutorial.springboot.cdcembeddeddebezium.MySqlUtils.*;

@SpringBootTest
@ActiveProfiles({"test", "mysql", "kafka", "embedded-debezium"})
class DebeziumTest {

    private final Faker fixture = new Faker();

    @Test
    void whenInsertIntoDatabase_thenReceivedMessageFromKafkaTopic() {

        var givenId = runMysqlNextId(CONTAINER_NAME, USERNAME, PASSWORD, DATABASE_NAME, SEQUENCE_NAME);
        var givenCode = fixture.number().randomDigit();
        var givenName = fixture.name().fullName();

        var query = new String[]{
                "USE tutorial_db;",
                String.format("INSERT INTO example_table (id, code, name, datetime) VALUES (%d, %d, '%s', CURRENT_TIMESTAMP);", givenId, givenCode, givenName)
        };

        int actual = runMysqlCommand(CONTAINER_NAME, USERNAME, PASSWORD, query);
        Assertions.assertEquals(0, actual);

        waitForChangeDataCapture();
    }

}
