package com.tutorial.springboot.monitorjaeger;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ApplicationApiTests {

    static final String HOST = "http://localhost";

    static final String ROOT_URI = "/api/v1/application";

    static final Logger LOGGER = LoggerFactory.getLogger(ApplicationApiTests.class.getSimpleName());

    @Container
    static final GenericContainer<?> JAEGER = new GenericContainer<>("jaegertracing/all-in-one:latest");

    static {
        try {
            JAEGER.setExposedPorts(List.of(16686, 4318));
        } catch (Exception e) {
            LOGGER.error("Start jaeger container failed due to: {}", e.getMessage());
        }
    }

    @BeforeAll
    static void beforeAll() {
        JAEGER.start();
    }

    @AfterAll
    static void afterAll() {
        JAEGER.stop();
    }

    @LocalServerPort
    int port;

    @Test
    public void testHelloEndpoint() {
        RestAssured.registerParser("text/plain", io.restassured.parsing.Parser.TEXT);
        RestAssured.given()
                .contentType(ContentType.JSON)
                .baseUri(HOST).port(port).basePath(ROOT_URI)
                .when().get("/status")
                .then()
                .statusCode(OK.value())
                .body(notNullValue())
                .body(equalTo("UP"))
        ;
    }
}
