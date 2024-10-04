package com.tutorial.springboot.security_rbac_jwt.api;

import com.tutorial.springboot.security_rbac_jwt.dto.CredentialsDto;
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
@ActiveProfiles({"test","h2"})
public class TokenApiTest {

    @LocalServerPort
    int port;

    @Test
    void givenValidCredentials_whenGenerateToken_thenReturnJwtTokenWithOKStatus() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .auth().basic("admin", "admin")
                .baseUri("http://localhost").port(port).basePath("/api/v1/token/new")
                .when().get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("token", not(emptyOrNullString()))
                .body("expiration", not(emptyOrNullString()))
                .log()
                .all();
    }

    @Test
    void givenInvalidCredentials_whenGenerateToken_thenReturnUnauthorizedStatus() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .auth().basic("admin", "wrong_password")
                .baseUri("http://localhost").port(port).basePath("/api/v1/token/new")
                .when().get()
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }
}