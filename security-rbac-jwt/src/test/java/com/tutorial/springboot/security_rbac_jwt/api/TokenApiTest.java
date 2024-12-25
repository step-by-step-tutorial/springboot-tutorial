package com.tutorial.springboot.security_rbac_jwt.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static com.tutorial.springboot.security_rbac_jwt.testutils.TestConstant.*;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles({"test", "h2"})
public class TokenApiTest {

    private static final String BASE_PATH = "/api/v1/token";

    @LocalServerPort
    private int port;

    @Test
    void givenValidUserCredentials_whenRequestingNewToken_thenReturnsJwtTokenAndStatusOK() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .auth().basic(TEST_USERNAME, TEST_PASSWORD)
                .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port).basePath(BASE_PATH + "/me/new")
                .when().get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("token", not(emptyOrNullString()))
                .body("expiration", not(emptyOrNullString()));
    }

    @Test
    void givenInvalidUserCredentials_whenRequestingNewToken_thenReturnsStatusUnauthorized() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .auth().basic("test", "wrong_password")
                .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port).basePath(BASE_PATH + "/me/new")
                .when().get()
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }
}