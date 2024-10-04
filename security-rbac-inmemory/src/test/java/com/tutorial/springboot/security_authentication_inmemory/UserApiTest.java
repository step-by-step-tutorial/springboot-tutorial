package com.tutorial.springboot.security_authentication_inmemory;

import com.tutorial.springboot.security_authentication_inmemory.dto.UserDto;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserApiTest {

    @Value("${local.server.port}")
    private int port;

    @Test
    public void givenNoUser_whenAuthentication_thenReturnUnauthorized() {
        given()
                .baseUri("http://localhost").port(port).basePath("/api/v1/users/me")
                .when().get()
                .then()
                .statusCode(401);
    }

    @Test
    public void givenUserCredentials_whenAuthentication_thenReturnOk() {
        var givenUsername = "user";
        var givenPassword = "password";
        given()
                .auth().basic(givenUsername, givenPassword)
                .baseUri("http://localhost").port(port).basePath("/api/v1/users/me")
                .when().get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("Current user is user"));
    }

    @Test
    public void givenAdminCredentials_whenAuthentication_thenReturnOk() {
        var givenUsername = "admin";
        var givenPassword = "password";
        given()
                .auth().basic(givenUsername, givenPassword)
                .baseUri("http://localhost").port(port).basePath("/api/v1/users/me")
                .when().get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("Current user is admin"));
    }

    @Test
    public void givenUser_whenSave_thenReturnCreateStatus() {
        var givenUsername = "admin";
        var givenPassword = "password";
        given()
                .auth().basic(givenUsername, givenPassword)
                .contentType(ContentType.JSON)
                .baseUri("http://localhost").port(port).basePath("/api/v1/users")
                .body(new UserDto().username("Saman").password("password").roles(List.of("USER", "admin")))
                .when().post()
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body(equalTo("User created"));
    }

    @Test
    public void givenUsername_whenFindByUsername_thenReturnUserDto() {
        var givenUsername = "user";
        var givenPassword = "password";
        given()
                .auth().basic(givenUsername, givenPassword)
                .baseUri("http://localhost").port(port)
                .basePath("/api/v1/users/{username}").pathParam("username", givenUsername)
                .when().get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("username", equalTo(givenUsername))
                .body("roles.size()", is(1))
                .body("roles", hasItems("ROLE_USER"));
    }
}
