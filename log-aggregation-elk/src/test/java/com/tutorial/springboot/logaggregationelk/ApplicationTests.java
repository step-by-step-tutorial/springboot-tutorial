package com.tutorial.springboot.logaggregationelk;

import io.restassured.RestAssured;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.CoreMatchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ApplicationTests {
    @LocalServerPort
    int port;

    @Test
    void sendLogToLogAggregator() {
        RestAssured.given()
                .contentType("application/json")
                .baseUri("http://localhost").port(port).basePath("/api/v1/health-check")
                .when().get()
                .then()
                .statusCode(200)
                .body(notNullValue());
    }

}
