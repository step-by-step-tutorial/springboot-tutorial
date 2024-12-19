package com.tutorial.springboot.securityoauth2server;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static com.tutorial.springboot.securityoauth2server.testutils.TestHttpUtils.TEST_HOSTNAME;
import static com.tutorial.springboot.securityoauth2server.testutils.TestTokenUtils.requestToGetNewTestUserToken;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles({"test", "h2"})
public class UserApiTest {

    private static final String BASE_PATH = "/api/v1/users";

    @LocalServerPort
    private int port;

    @Nested
    class GetUserResourceTests {
        @Test
        void givenToken_whenRequestToResource_thenReturnResourceWithOKStatus() {
            var givenToken = requestToGetNewTestUserToken(port);

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri("http://" + TEST_HOSTNAME).port(port).basePath(BASE_PATH)
                    .when().get()
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("size()", is(3));
        }
    }
}