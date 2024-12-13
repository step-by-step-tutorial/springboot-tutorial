package com.tutorial.springboot.securityoauth2server;

import com.tutorial.springboot.securityoauth2server.dto.ClientDto;
import com.tutorial.springboot.securityoauth2server.entity.Client;
import com.tutorial.springboot.securityoauth2server.test_utils.stub.ResultHelper;
import com.tutorial.springboot.securityoauth2server.test_utils.stub.TestClientAssistant;
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

import static com.tutorial.springboot.securityoauth2server.test_utils.SecurityTestUtils.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles({"test", "h2"})
public class TokenApiTest {

    static final String BASE_PATH = "/api/v1/token/me";

    @LocalServerPort
    int port;

    @Autowired
    TestClientAssistant testAssistant;

    @Test
    void givenCredentials_whenRequestTokenForUser_thenReturnJwtTokenWithOKStatus() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .auth().basic(TEST_USERNAME, TEST_PASSWORD)
                .baseUri("http://" + TEST_HOSTNAME).port(port).basePath(BASE_PATH + "/new")
                .when().get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("token", not(emptyOrNullString()))
                .body("expiration", not(emptyOrNullString()));
    }

    @Test
    void givenToken_whenRequestToResource_thenReturnResourceWithOKStatus() {
       var givenToken = requestToGetTestToken();

        RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + givenToken)
                .baseUri("http://" + TEST_HOSTNAME).port(port).basePath("/api/v1/users")
                .when().get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(3));
    }

   @Nested
    class ClientTokenTests {

       @BeforeEach
       void populate() {
           loginToTestEnv();
           testAssistant.insertTestClient(1);
       }

       @Test
       void givenClientId_whenRequestTokenForClient_thenReturnJwtTokenWithOKStatus() {
           var givenClientId = testAssistant.selectTestClient().dto().asOne().getClientId();

           RestAssured.given()
                   .contentType(ContentType.JSON)
                   .auth().basic(TEST_USERNAME, TEST_PASSWORD)
                   .baseUri("http://" + TEST_HOSTNAME).port(port)
                   .basePath(BASE_PATH + "/{clientId}").pathParam("clientId", givenClientId)
                   .when().get()
                   .then()
                   .statusCode(HttpStatus.OK.value())
                   .body("token", not(emptyOrNullString()))
                   .body("expiration", not(emptyOrNullString()));
       }
   }

}