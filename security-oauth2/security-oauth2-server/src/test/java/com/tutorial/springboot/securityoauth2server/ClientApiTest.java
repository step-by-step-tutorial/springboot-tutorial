package com.tutorial.springboot.securityoauth2server;

import com.tutorial.springboot.securityoauth2server.testutils.stub.assistant.ClientTestAssistant;
import com.tutorial.springboot.securityoauth2server.testutils.stub.factory.ClientTestFactory;
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

import static com.tutorial.springboot.securityoauth2server.testutils.SecurityTestUtils.loginToTestEnv;
import static com.tutorial.springboot.securityoauth2server.testutils.TestUtils.TEST_PASSWORD;
import static com.tutorial.springboot.securityoauth2server.testutils.TestUtils.TEST_USERNAME;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles({"test", "h2"})
public class ClientApiTest {

    private static final String APP_HOST = "http://localhost";

    private static final String BASE_PATH = "/api/v1/clients";

    @LocalServerPort
    private int port;

    @Autowired
    private ClientTestAssistant assistant;

    @Autowired
    private ClientTestFactory factory;

    @Nested
    class SaveOneTests {
        @Test
        void givenClient_whenSaveOne_thenReturnIdWithCreatedStatus() {
            var givenBody = factory.newOne();

            var actual = assertDoesNotThrow(() -> {
                RestAssured.given()
                        .contentType(ContentType.JSON)
                        .auth().basic(TEST_USERNAME, TEST_PASSWORD)
                        .baseUri(APP_HOST).port(port).basePath(BASE_PATH)
                        .body(givenBody)
                        .when().post()
                        .then()
                        .statusCode(HttpStatus.CREATED.value())
                        .header("Location", containsString(BASE_PATH))
                        .body("", not(emptyOrNullString()))
                        .body("", greaterThan(0));

                return assistant.selectAllTest().dto().asOne();
            });

            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertNotNull(actual.getCreatedBy());
            assertNotNull(actual.getCreatedAt());
            assertNotNull(actual.getVersion());

        }
    }

    @Nested
    class FindByClientIdTests {

        private static final String RELATIVE_PATH = "/findByClientId/{clientId}";

        @BeforeEach
        void setUp() {
            assistant.populate(1);
        }

        @Test
        void givenClientId_whenFindById_thenReturnDtoWithOKStatus() {
            var givenClientId = assistant.selectAllTest().dto().asOne().getClientId();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .auth().basic(TEST_USERNAME, TEST_PASSWORD)
                    .baseUri(APP_HOST).port(port)
                    .basePath(BASE_PATH + RELATIVE_PATH).pathParam("clientId", givenClientId)
                    .when().get()
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("", notNullValue())
                    .body("clientId", equalTo(givenClientId))
                    .body("clientSecret", emptyOrNullString())
                    .body("redirectUri", equalTo("http://localhost:8080/login/oauth2/code/" + givenClientId))
                    .body("grantTypes", hasSize(6))
                    .body("scopes", hasSize(2))
                    .body("accessTokenValiditySeconds", equalTo(3600))
                    .body("refreshTokenValiditySeconds", equalTo(1209600));
        }
    }

    @Nested
    class FindAllClientsTests {

        @BeforeEach
        void setUp() {
            assistant.populate(2);
        }

        @Test
        void givenNothing_whenFindAll_thenReturnListOfDtoWithOkStatus() {
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .auth().basic(TEST_USERNAME, TEST_PASSWORD)
                    .baseUri(APP_HOST).port(port).basePath(BASE_PATH)
                    .when().get()
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("", notNullValue())
                    .body("", hasSize(2));
        }
    }
}
