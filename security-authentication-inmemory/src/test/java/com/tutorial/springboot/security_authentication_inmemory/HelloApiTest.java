package com.tutorial.springboot.security_authentication_inmemory;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HelloApiTest {

    @Value("${local.server.port}")
    private int port;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
    }

    @Test
    public void givenNoUser_whenAuthentication_thenReturnUnauthorized() {
        given()
                .when().get("/")
                .then().statusCode(401);
    }

    @Test
    public void givenUserCredentials_whenAuthentication_thenReturnOk() {
        given()
                .auth().basic("user", "password")
                .when().get("/")
                .then().statusCode(200).body(equalTo("Hello, World!"));
    }

    @Test
    public void givenAdminCredentials_whenAuthentication_thenReturnOk() {
        given()
                .auth().basic("admin", "admin")
                .when().get("/")
                .then().statusCode(200).body(equalTo("Hello, World!"));
    }
}
