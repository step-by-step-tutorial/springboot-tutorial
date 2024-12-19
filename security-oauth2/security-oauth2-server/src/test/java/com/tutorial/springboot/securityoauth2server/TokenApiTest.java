package com.tutorial.springboot.securityoauth2server;

import com.tutorial.springboot.securityoauth2server.testutils.stub.assistant.ClientTestAssistant;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static com.tutorial.springboot.securityoauth2server.testutils.TestHttpUtils.*;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles({"test", "h2"})
public class TokenApiTest {

    private static final String BASE_PATH = "/api/v1/token/me";

    @LocalServerPort
    private int port;

    @Autowired
    private ClientTestAssistant assistant;

    @Nested
    class UserTokenTests {

        private static final String RELATIVE_PATH = "/new";

        @Test
        void givenCredentials_whenRequestGenerateNewToken_thenReturnJwtTokenWithOKStatus() {
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .auth().basic(TEST_USERNAME, TEST_PASSWORD)
                    .baseUri("http://" + TEST_HOSTNAME).port(port).basePath(BASE_PATH + RELATIVE_PATH)
                    .when().get()
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("token", not(emptyOrNullString()))
                    .body("expiration", not(emptyOrNullString()));
        }
    }

    @Nested
    class ClientTokenTests {

        private static final String RELATIVE_PATH = "/client/{clientId}";

        @BeforeEach
        void populate() {
            assistant.populate(1);
        }

        @Test
        void givenClientId_whenRequestGetTokenBelongingToClient_thenReturnJwtTokenWithOKStatus() {
            var givenClientId = assistant.selectAllTest().dto().asOne().getClientId();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .auth().basic(TEST_USERNAME, TEST_PASSWORD)
                    .baseUri("http://" + TEST_HOSTNAME).port(port)
                    .basePath(BASE_PATH + RELATIVE_PATH).pathParam("clientId", givenClientId)
                    .when().get()
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("token", not(emptyOrNullString()))
                    .body("expiration", not(emptyOrNullString()));
        }
    }

}