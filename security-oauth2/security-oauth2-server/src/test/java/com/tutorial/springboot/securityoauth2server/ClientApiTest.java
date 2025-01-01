package com.tutorial.springboot.securityoauth2server;

import com.tutorial.springboot.securityoauth2server.fixture.client.ClientDtoFixture;
import com.tutorial.springboot.securityoauth2server.fixture.client.ClientEntityFixture;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static com.tutorial.springboot.securityoauth2server.testutils.TestAuthenticationHelper.login;
import static com.tutorial.springboot.securityoauth2server.testutils.TestConstant.*;
import static com.tutorial.springboot.securityoauth2server.testutils.TestTokenUtils.requestToGetNewToken;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles({"test", "h2"})
public class ClientApiTest {

    private static final String BASE_PATH = "/api/v1/clients";

    @LocalServerPort
    private int port;

    @Autowired
    private EntityManagerFactory assistant;

    @BeforeEach
    void setUp() {
        login();
    }


    @Nested
    class SaveOneTests {
        @Test
        void givenClient_whenSaveOne_thenReturnIdWithCreatedStatus() {
            var givenBody = ClientDtoFixture.newGivenClient();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .auth().basic(TEST_USERNAME, TEST_PASSWORD)
                    .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port).basePath(BASE_PATH)
                    .body(givenBody)
                    .when().post()
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .header("Location", containsString(BASE_PATH))
                    .body("", not(emptyOrNullString()))
                    .body("", greaterThan(0));
        }
    }

    @Nested
    class FindByClientIdTests {

        private static final String RELATIVE_PATH = "/findByClientId/{clientId}";

        @Test
        void givenClientId_whenFindById_thenReturnDtoWithOKStatus() {
            var client = ClientEntityFixture.persistedGivenClient(assistant, "testClientId");
            var givenClientId = client.getClientId();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .auth().basic(TEST_USERNAME, TEST_PASSWORD)
                    .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port)
                    .basePath(BASE_PATH + RELATIVE_PATH).pathParam("clientId", givenClientId)
                    .when().get()
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("", notNullValue())
                    .body("clientId", equalTo(givenClientId))
                    .body("clientSecret", emptyOrNullString())
                    .body("redirectUri", equalTo("http://localhost:8080/login/oauth2/code/" + givenClientId))
                    .body("grantTypes", hasSize(6))
                    .body("scopes", hasSize(4))
                    .body("accessTokenValiditySeconds", equalTo(3600))
                    .body("refreshTokenValiditySeconds", equalTo(1209600));
        }
    }

    @Nested
    class FindAllClientsTests {

        @BeforeEach
        void setUp() {
            ClientEntityFixture.persistedGivenClient(assistant);
        }

        @Test
        void givenNothing_whenFindAll_thenReturnListOfDtoWithOkStatus() {
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .auth().basic(TEST_USERNAME, TEST_PASSWORD)
                    .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port).basePath(BASE_PATH)
                    .when().get()
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("", notNullValue())
                    .body("", hasSize(1));
        }
    }
}
