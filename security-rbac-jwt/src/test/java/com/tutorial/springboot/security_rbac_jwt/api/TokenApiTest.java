package com.tutorial.springboot.security_rbac_jwt.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static com.tutorial.springboot.security_rbac_jwt.testutils.TestHttpUtils.*;
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
    void givenValidCredentials_whenGenerateToken_thenReturnJwtTokenWithOKStatus() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .auth().basic(TEST_USERNAME, TEST_PASSWORD)
                .baseUri("http://" + TEST_HOSTNAME).port(port).basePath(BASE_PATH + "/me/new")
                .when().get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("token", not(emptyOrNullString()))
                .body("expiration", not(emptyOrNullString()))
                .log()
                .all(true);
    }

    @Test
    void givenInvalidCredentials_whenGenerateToken_thenReturnUnauthorizedStatus() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .auth().basic("test", "wrong_password")
                .baseUri("http://" + TEST_HOSTNAME).port(port).basePath(BASE_PATH + "/me/new")
                .when().get()
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }
}