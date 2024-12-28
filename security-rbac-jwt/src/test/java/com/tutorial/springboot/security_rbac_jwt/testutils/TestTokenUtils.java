package com.tutorial.springboot.security_rbac_jwt.testutils;

import com.tutorial.springboot.security_rbac_jwt.dto.UserDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.springframework.http.HttpStatus;

import static com.tutorial.springboot.security_rbac_jwt.testutils.TestConstant.*;
import static org.hamcrest.Matchers.*;

public final class TestTokenUtils {

    public static final String TOKEN_API_BASE_PATH = "/api/v1/token";

    private static final String USER_BASE_PATH = "/api/v1/users";

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
                .baseUri(TEST_PROTOCOL + hostname).port(port).basePath(TOKEN_API_BASE_PATH.concat("/me/new"))
                .when().get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("token", not(emptyOrNullString()))
                .body("expiration", not(emptyOrNullString()))
                .extract()
                .jsonPath().getString("token");
    }

    public static long saveUserThroughApi(int port, UserDto user) {
        var givenToken = requestToGetNewToken(port);

        var userId = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + givenToken)
                .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port).basePath(USER_BASE_PATH)
                .body(user)
                .when().post()
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", containsString(USER_BASE_PATH))
                .body("", notNullValue())
                .extract().asString();

        return Long.parseLong(userId);

    }

}
