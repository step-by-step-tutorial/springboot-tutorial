package com.tutorial.springboot.securityoauth2server;

import com.tutorial.springboot.securityoauth2server.dto.ClientDto;
import com.tutorial.springboot.securityoauth2server.dto.UserDto;
import com.tutorial.springboot.securityoauth2server.enums.GrantType;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles({"test", "h2"})
public class ClientApiTest {

    @LocalServerPort
    int port;

    @Test
    void givenClient_whenSaveOne_thenReturnIdWithCreatedStatus() {
        var givenUsername = "admin";
        var givenPassword = "admin";
        var givenBody = new ClientDto()
                .setClientId("test-client")
                .setClientSecret("test-secret")
                .setRedirectUri("http://localhost:8080/login/oauth2/code/test-client")
                .setGrantTypes(GrantType.toList())
                .setScopes(Arrays.asList("read", "write"))
                .setAccessTokenValiditySeconds(3600)
                .setRefreshTokenValiditySeconds(1209600);

        var uri = RestAssured.given()
                .contentType(ContentType.JSON)
                .auth().basic(givenUsername, givenPassword)
                .baseUri("http://localhost").port(port).basePath("/api/v1/clients")
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

    @Test
    void givenClient_whenGetAll_thenReturnIdWithCreatedStatus() {
        var givenUsername = "admin";
        var givenPassword = "admin";

        RestAssured.given()
                .contentType(ContentType.JSON)
                .auth().basic(givenUsername, givenPassword)
                .baseUri("http://localhost").port(port).basePath("/api/v1/clients")
                .when().get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("", notNullValue())
                .log().all(true);

    }


}