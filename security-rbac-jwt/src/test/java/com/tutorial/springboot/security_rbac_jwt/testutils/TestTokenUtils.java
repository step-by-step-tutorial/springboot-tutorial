package com.tutorial.springboot.security_rbac_jwt.testutils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.springframework.http.HttpStatus;

import static com.tutorial.springboot.security_rbac_jwt.testutils.TestHttpUtils.*;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;

public final class TestTokenUtils {

    public static final String TOKEN_API_BASE_PATH = "/api/v1/token";

    private TestTokenUtils() {
    }

    public static String requestToGetNewToken() {
        return requestToGetNewToken(TEST_HOSTNAME, TEST_PORT, TEST_USERNAME, TEST_PASSWORD);
    }

    public static String requestToGetNewToken(int port) {
        return requestToGetNewToken(TEST_HOSTNAME, port, TEST_USERNAME, TEST_PASSWORD);
    }

    public static String requestToGetNewToken(String hostname, int port, String username, String password) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .auth().basic(username, password)
                .baseUri("http://" + hostname).port(port).basePath(TOKEN_API_BASE_PATH.concat("/me/new"))
                .when().get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("token", not(emptyOrNullString()))
                .body("expiration", not(emptyOrNullString()))
                .extract()
                .jsonPath().getString("token");
    }

}
