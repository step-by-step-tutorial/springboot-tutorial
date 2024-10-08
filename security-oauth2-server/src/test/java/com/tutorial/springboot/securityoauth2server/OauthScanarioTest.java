package com.tutorial.springboot.securityoauth2server;

import com.tutorial.springboot.securityoauth2server.dto.ClientDto;
import com.tutorial.springboot.securityoauth2server.enums.GrantType;
import com.tutorial.springboot.securityoauth2server.service.impl.TokenService;
import com.tutorial.springboot.securityoauth2server.test_utils.stub.DtoStubFactory;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles({"test", "h2"})
public class OauthScanarioTest {

    final Logger logger = LoggerFactory.getLogger(TokenApiTest.class.getSimpleName());

    final String serverUrl = "http://localhost";

    @LocalServerPort
    int port;

    @Autowired
    TokenService tokenService;

    private String requestToGetToken() {
        var userName = "admin";
        var password = "admin";
        var token = RestAssured.given()
                .contentType(ContentType.JSON)
                .auth().basic(userName, password)
                .baseUri(serverUrl).port(port).basePath("/api/v1/token/me/new")
                .when().get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("token", not(emptyOrNullString()))
                .body("expiration", not(emptyOrNullString()))
                .log().all(true)
                .extract()
                .jsonPath().getString("token");

        logger.info("New JWT token generated, username:{}, roles:{}",
                tokenService.extractUsername(token),
                tokenService.extractRoles(token)
        );

        return token;
    }

    private String requestToGetTokenOfClient(String clientId) {
        var userName = "admin";
        var password = "admin";
        var token = RestAssured.given()
                .contentType(ContentType.JSON)
                .auth().basic(userName, password)
                .baseUri(serverUrl).port(port)
                .basePath("/api/v1/token/me/{clientId}").pathParam("clientId", clientId)
                .when().get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("token", not(emptyOrNullString()))
                .body("expiration", not(emptyOrNullString()))
                .log().all(true)
                .extract()
                .jsonPath().getString("token");

        logger.info("New JWT token generated, username:{}, roles:{}, clientId:{}",
                tokenService.extractUsername(token),
                tokenService.extractRoles(token),
                tokenService.extractClientId(token)
        );

        return token;
    }

    private String registerClient() {
        var username = "admin";
        var password = "admin";
        var client = DtoStubFactory.createClientOfJwtBearer(1).asOne();

        RestAssured.given()
                .contentType(ContentType.JSON)
                .auth().basic(username, password)
                .baseUri(serverUrl).port(port).basePath("/api/v1/clients")
                .body(client)
                .when().post()
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", containsString("/api/v1/clients"))
                .body("", notNullValue())
                .log().all(true);

        logger.info("New client registered: {}", client.getClientId());
        return client.getClientId();
    }

    /**
     * 1. ==    Send token request      ==>
     * 2. <==   Get access token        ==
     * 3. ==    Send resource request   ==>
     * 4. <==   Get resource            ==
     */
    @Test
    void getToken_getRequest() {
        // round 1: send request to get JWT Bearer token
        var givenToken = requestToGetToken();

        // round 2: send request to get protected resource
        RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", String.format("Bearer %s", givenToken))
                .baseUri(serverUrl).port(port).basePath("/api/v1/health")
                .when().get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("UP"))
                .log().all(true);
    }

    /**
     * 1. ==    Send grant type request   ==>
     * 2. <==   Get grant type            ==
     * 3. ==    Send token request        ==>
     * 4. <==   Get access token          ==
     * 5. ==    Send resource request     ==>
     * 6. <==   Get resource              ==
     */
    @Test
    void getGrantType_getToken_getRequest() {
        // round 1: send request to get grant type.
        // It is possible to access grant types with client ID of registered a client.
        var clientId = registerClient();

        // round 2: send request to get JWT Bearer token
        // It is possible to generate token based grant types with client ID of registered a client.
        var givenToken = requestToGetTokenOfClient(clientId);

        // round 3: send request to get protected resource
        RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", String.format("Bearer %s", givenToken))
                .baseUri(serverUrl).port(port).basePath("/api/v1/health")
                .when().get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("UP"))
                .log().all(true);
    }

    @Test
    void givenClient_whenSaveOne_thenReturnIdWithCreatedStatus() {
        var givenUsername = "admin";
        var givenPassword = "admin";

        var givenBody = new ClientDto()
                .setClientId("test-client")
                .setClientSecret("test-secret")
                .setRedirectUri("http://localhost:8080/login/oauth2/code/test-client")
                .setGrantTypes(GrantType.allType())
                .setScopes(Arrays.asList("read", "write"))
                .setAccessTokenValiditySeconds(3600)
                .setRefreshTokenValiditySeconds(1209600);

        var uri = RestAssured.given()
                .contentType(ContentType.JSON)
                .auth().basic(givenUsername, givenPassword)
                .baseUri(serverUrl).port(port).basePath("/api/v1/clients")
                .body(givenBody)
                .when().post()
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", containsString("/api/v1/clients"))
                .body("", notNullValue())
                .log().all()
                .extract()
                .header("Location");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .auth().basic(givenUsername, givenPassword)
                .baseUri(uri).port(port)
                .when().get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("", notNullValue())
                .log().all(true);

    }
}