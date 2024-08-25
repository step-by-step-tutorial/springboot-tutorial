package com.tutorial.springboot.rbac.api;

import com.tutorial.springboot.rbac.dto.CredentialsDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class AuthControllerTest {

    @LocalServerPort
    int port;

    @Test
    void givenValidCredentials_whenAuthenticate_thenReturnJwtTokenWithOKStatus() {
        var givenCredentials = new CredentialsDto("admin", "admin");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .baseUri("http://localhost").port(port).basePath("/api/v1/auth/login")
                .body(givenCredentials)
                .when().post()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("token", not(emptyOrNullString()))
                .body("expiration", not(emptyOrNullString()));
    }

    @Test
    void givenInvalidCredentials_whenAuthenticate_thenReturnUnauthorizedStatus() {
        var givenCredentials = new CredentialsDto("admin", "wrong_password");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .baseUri("http://localhost").port(port).basePath("/api/v1/auth/login")
                .body(givenCredentials)
                .when().post()
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .body("errors[0]", equalTo("Bad credentials"));
    }
}