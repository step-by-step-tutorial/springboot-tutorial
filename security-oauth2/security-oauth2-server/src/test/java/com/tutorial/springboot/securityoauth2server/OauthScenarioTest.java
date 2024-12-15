package com.tutorial.springboot.securityoauth2server;

import com.tutorial.springboot.securityoauth2server.dto.ClientDto;
import com.tutorial.springboot.securityoauth2server.testutils.stub.factory.ClientTestFactory;
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

import java.util.Base64;

import static com.tutorial.springboot.securityoauth2server.testutils.HttpTestUtils.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles({"test", "h2"})
public class OauthScenarioTest {

    private final Logger logger = LoggerFactory.getLogger(TokenApiTest.class.getSimpleName());

    @LocalServerPort
    private int port;

    @Autowired
    private ClientTestFactory clientFactory;

    private String requestToGetNewTestUserToken() {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .auth().basic(TEST_USERNAME, TEST_PASSWORD)
                .baseUri("http://" + TEST_HOSTNAME).port(port)
                .basePath("/api/v1/token/me/new")
                .when().get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("token", not(emptyOrNullString()))
                .body("expiration", not(emptyOrNullString()))
                .extract()
                .jsonPath().getString("token");
    }

    private String requestToGetTokenOfClient(String clientId) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .auth().basic(TEST_USERNAME, TEST_PASSWORD)
                .baseUri("http://" + TEST_HOSTNAME).port(port)
                .basePath("/api/v1/token/me/client/{clientId}").pathParam("clientId", clientId)
                .when().get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("token", not(emptyOrNullString()))
                .body("expiration", not(emptyOrNullString()))
                .extract()
                .jsonPath().getString("token");
    }

    private void requestToRegisterClient(ClientDto client) {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .auth().basic(TEST_USERNAME, TEST_PASSWORD)
                .baseUri("http://" + TEST_HOSTNAME).port(port).basePath("/api/v1/clients")
                .body(client)
                .when().post()
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", containsString("/api/v1/clients"))
                .body("", not(emptyOrNullString()))
                .body("", greaterThan(0));
    }

    private String requestToGetProtectedResource(String givenToken) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", String.format("Bearer %s", givenToken))
                .baseUri("http://" + TEST_HOSTNAME).port(port).basePath("/api/v1/health")
                .when().get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("status", equalTo("UP"))
                .extract().body().asString();
    }

    private String createLoginSession() {
        return RestAssured.given()
                .contentType(ContentType.URLENC)
                .formParam("username", TEST_USERNAME)
                .formParam("password", TEST_PASSWORD)
                .baseUri("http://" + TEST_HOSTNAME).port(TEST_PORT).basePath("/login")
                .redirects().follow(false)
                .post()
                .then()
                .header("Location", not(emptyOrNullString()))
                .extract()
                .response()
                .getCookie("JSESSIONID");
    }

    private String requestAuthorizationCode(ClientDto client, String jsessionid) {
        var redirectUrl = RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("JSESSIONID", jsessionid)
                .baseUri("http://" + TEST_HOSTNAME).port(TEST_PORT).basePath("/oauth2/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", client.getClientId())
                .queryParam("scope", "read")
                .queryParam("redirect_uri", client.getRedirectUri())
                .redirects().follow(false)
                .when().get()
                .then()
                .header("Location", not(emptyOrNullString()))
                .extract()
                .response()
                .getHeader("Location");

        return extractAuthorizationCodeFromUrl(redirectUrl);
    }

    private String requestToGetTokenByAuthorizationCode(ClientDto client, String authorizationCode) {
        return RestAssured.given()
                .contentType(ContentType.URLENC)
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((client.getClientId() + ":" + client.getClientSecret()).getBytes()))
                .baseUri("http://" + TEST_HOSTNAME).port(TEST_PORT).basePath("/oauth2/token")
                .formParam("grant_type", "authorization_code")
                .formParam("code", authorizationCode)
                .formParam("redirect_uri", client.getRedirectUri())
                .when().post()
                .then()
                .body("access_token", not(emptyOrNullString()))
                .body("refresh_token", not(emptyOrNullString()))
                .body("scope", equalTo("read"))
                .body("token_type", equalTo("Bearer"))
                .body("expires_in", greaterThan(0))
                .extract()
                .jsonPath().getString("access_token");
    }

    /**
     * 1. ===   Send token request      ==>
     * 2. <==   Get access token        ===
     * 3. ===   Send resource request   ==>
     * 4. <==   Get resource            ===
     */
    @Test
    void getToken_getRequest() {
        // round 1: send request to get JWT Bearer token
        var givenToken = requestToGetNewTestUserToken();

        // round 2: send request to get protected resource
        var actual = requestToGetProtectedResource(givenToken);
        assertNotNull(actual);
    }

    /**
     * 1. ===   Send register request     ==>
     * 2. <==   Get client info           ===
     * 3. ===   Send token request        ==>
     * 4. <==   Get access token          ===
     * 5. ===   Send resource request     ==>
     * 6. <==   Get resource              ===
     */
    @Test
    void registerClient_getToken_getRequest() {
        // Preparation
        var client = clientFactory.newOne();

        // round 1: send request to register client.
        requestToRegisterClient(client);

        // round 2: send request to get JWT Bearer token
        var givenToken = requestToGetTokenOfClient(client.getClientId());

        // round 3: send request to get protected resource
        var actual = requestToGetProtectedResource(givenToken);
        assertNotNull(actual);
    }

    /**
     * 1. ===   Send grant type request   ==>
     * 2. <==   Get grant type            ===
     * 3. ===   Send token request        ==>
     * 4. <==   Get access token          ===
     * 5. ===   Send resource request     ==>
     * 6. <==   Get resource              ===
     */
    @Test
    void getGrantType_getToken_getRequest() {
        // Preparation
        var givenClient = clientFactory.newOne();

        // round 1: send request to get grant type.
        requestToRegisterClient(givenClient);
        var jsessionid = createLoginSession();
        var authorizationCode = requestAuthorizationCode(givenClient, jsessionid);

        // round 2: send request to get JWT Bearer token
        var token = requestToGetTokenByAuthorizationCode(givenClient, authorizationCode);

        // round 3: send request to get protected resource
        var actual = requestToGetProtectedResource(token);
        assertNotNull(actual);
    }

}