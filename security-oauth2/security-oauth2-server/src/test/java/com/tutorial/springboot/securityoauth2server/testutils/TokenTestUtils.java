package com.tutorial.springboot.securityoauth2server.testutils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.springframework.http.HttpStatus;

import static com.tutorial.springboot.securityoauth2server.testutils.TestUtils.*;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;

public final class TokenTestUtils {

    private TokenTestUtils() {
    }

    public static String requestToGetNewTestUserToken() {
        return requestToGetNewTestUserToken(TEST_HOSTNAME, TEST_PORT, TEST_USERNAME, TEST_PASSWORD);
    }

    public static String requestToGetNewTestUserToken(int port) {
        return requestToGetNewTestUserToken(TEST_HOSTNAME, port, TEST_USERNAME, TEST_PASSWORD);
    }

    public static String requestToGetNewTestUserToken(String hostname, int port, String username, String password) {
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

    public static String requestToGetTokenOfClient(String clientId) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .auth().basic(TEST_USERNAME, TEST_PASSWORD)
                .baseUri("http://" + TEST_HOSTNAME).port(TEST_PORT)
                .basePath("/api/v1/token/me/{clientId}").pathParam("clientId", clientId)
                .when().get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("token", not(emptyOrNullString()))
                .body("expiration", not(emptyOrNullString()))
                .extract()
                .jsonPath().getString("token");
    }

}
