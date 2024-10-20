package com.tutorial.springboot.securityoauth2client;

import com.tutorial.springboot.securityoauth2client.dto.ClientDto;
import com.tutorial.springboot.securityoauth2client.enums.GrantType;
import com.tutorial.springboot.securityoauth2client.service.impl.TokenService;
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
public class TokenApiTest {

    final Logger logger = LoggerFactory.getLogger(TokenApiTest.class.getSimpleName());

    @LocalServerPort
    int port;

    @Autowired
    TokenService tokenService;

    @Test
    void givenCredentials_whenRequestTokenForUser_thenReturnJwtTokenWithOKStatus() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .auth().basic("test", "test")
                .baseUri("http://localhost").port(port).basePath("/api/v1/token/me/new")
                .when().get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("token", not(emptyOrNullString()))
                .body("expiration", not(emptyOrNullString()))
                .log().all();
    }

    @Test
    void givenToken_whenRequestToResource_thenReturnResourceWithOKStatus() {
        var givenToken = RestAssured.given()
                .contentType(ContentType.JSON)
                .auth().basic("test", "test")
                .baseUri("http://localhost").port(port).basePath("/api/v1/token/me/new")
                .when().get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("token", not(emptyOrNullString()))
                .body("expiration", not(emptyOrNullString()))
                .log().all()
                .extract()
                .jsonPath().getString("token");

        logger.info("username = {}", tokenService.extractUsername(givenToken));
        logger.info("roles = {}", tokenService.extractRoles(givenToken));

        RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + givenToken)
                .baseUri("http://localhost").port(port).basePath("/api/v1/users")
                .when().get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(3))
                .log().all(true);
    }

    @Test
    void givenClientId_whenRequestTokenForClient_thenReturnJwtTokenWithOKStatus() {
        var givenUsername = "test";
        var givenPassword = "test";
        var givenBody = new ClientDto()
                .setClientId("test-client")
                .setClientSecret("test-secret")
                .setRedirectUri("http://localhost:8080/login/oauth2/code/test-client")
                .setGrantTypes(GrantType.allType())
                .setScopes(Arrays.asList("read", "write"))
                .setAccessTokenValiditySeconds(3600)
                .setRefreshTokenValiditySeconds(1209600);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .auth().basic(givenUsername, givenPassword)
                .baseUri("http://localhost").port(port).basePath("/api/v1/clients")
                .body(givenBody)
                .when().post()
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", containsString("/api/v1/clients"))
                .body("", notNullValue());

        RestAssured.given()
                .contentType(ContentType.JSON)
                .auth().basic("test", "test")
                .baseUri("http://localhost").port(port)
                .basePath("/api/v1/token/me/{clientId}").pathParam("clientId", "test-client")
                .when().get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("token", not(emptyOrNullString()))
                .body("expiration", not(emptyOrNullString()))
                .log().all();
    }

}