package com.tutorial.springboot.security_authentication_inmemory;

import com.tutorial.springboot.security_authentication_inmemory.dto.UserDto;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserApiTest {

    @Value("${local.server.port}")
    private int port;

    @Test
    public void givenNoUser_whenAuthentication_thenReturnUnauthorized() {
        given()
                .baseUri("http://localhost").port(port).basePath("/api/v1/users")
                .when().get()
                .then()
                .statusCode(401);
    }

    @Test
    public void givenUserCredentials_whenAuthentication_thenReturnOk() {
        given()
                .auth().basic("user", "password")
                .baseUri("http://localhost").port(port).basePath("/api/v1/users")
                .when().get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("Hello, user!"));
    }

    @Test
    public void givenAdminCredentials_whenAuthentication_thenReturnOk() {
        given()
                .auth().basic("admin", "password")
                .baseUri("http://localhost").port(port).basePath("/api/v1/users")
                .when().get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("Hello, admin!"));
    }

    @Test
    public void givenUser_whenSave_thenReturnCreateStatus() {
        given()
                .auth().basic("admin", "password")
                .contentType(ContentType.JSON)
                .baseUri("http://localhost").port(port).basePath("/api/v1/users")
                .body(new UserDto().username("Saman").password("password").roles(List.of("USER", "admin")))
                .when().post()
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body(equalTo("User created"));
    }
}
