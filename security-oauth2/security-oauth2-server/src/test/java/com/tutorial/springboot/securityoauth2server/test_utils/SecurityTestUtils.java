package com.tutorial.springboot.securityoauth2server.test_utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;

public final class SecurityTestUtils {

    public static final String TEST_HOSTNAME = "localhost";

    public static final int TEST_PORT = 8080;

    public static final String TEST_USERNAME = "test";

    public static final String TEST_PASSWORD = "test";

    private SecurityTestUtils() {
    }

    public static String requestToGetTestToken() {
        return requestToGetTestToken(TEST_HOSTNAME, TEST_PORT, TEST_USERNAME, TEST_PASSWORD);
    }

    public static String requestToGetTestToken(int port) {
        return requestToGetTestToken(TEST_HOSTNAME, port, TEST_USERNAME, TEST_PASSWORD);
    }

    public static String requestToGetTestToken(String hostname, int port, String username, String password) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .auth().basic(username, password)
                .baseUri("http://" + hostname).port(port).basePath("/api/v1/token/me/new")
                .when().get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("token", not(emptyOrNullString()))
                .body("expiration", not(emptyOrNullString()))
                .extract()
                .jsonPath().getString("token");
    }

    public static void loginToTestEnv() {
        loginToTestEnv(TEST_USERNAME, TEST_PASSWORD);
    }

    public static void loginToTestEnv(String username, String password) {
        var auth = new UsernamePasswordAuthenticationToken(username, password);
        SecurityContextHolder.setContext(new SecurityContextImpl(auth));
    }
}
