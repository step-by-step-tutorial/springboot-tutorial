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
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void givenUserCredentials_whenAuthentication_thenReturnOk() {
        var givenAuthUsername = "user";
        var givenAuthPassword = "password";

        given()
                .auth().basic(givenAuthUsername, givenAuthPassword)
                .baseUri("http://localhost").port(port).basePath("/api/v1/users/me")
                .when().get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("Current user is [user]"));
    }

    @Test
    public void givenAdminCredentials_whenAuthentication_thenReturnOk() {
        var givenAuthUsername = "admin";
        var givenAuthPassword = "password";

        given()
                .auth().basic(givenAuthUsername, givenAuthPassword)
                .baseUri("http://localhost").port(port).basePath("/api/v1/users/me")
                .when().get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("Current user is [admin]"));
    }

    @Test
    public void givenUser_whenSave_thenReturnCreateStatus() {
        var givenAuthUsername = "admin";
        var givenAuthPassword = "password";
        var givenNewUser = new UserDto().username("test").password("password").roles(List.of("USER", "admin"));

        given()
                .auth().basic(givenAuthUsername, givenAuthPassword)
                .contentType(ContentType.JSON)
                .baseUri("http://localhost").port(port).basePath("/api/v1/users")
                .body(givenNewUser)
                .when().post()
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body(equalTo("User [test] created"));
    }


    @Test
    public void givenExistsUser_whenSave_thenReturnBadRequestError() {
        var givenAuthUsername = "admin";
        var givenAuthPassword = "password";
        var givenNewUser = new UserDto().username("user").password("password").roles(List.of("USER", "admin"));

        given()
                .auth().basic(givenAuthUsername, givenAuthPassword)
                .contentType(ContentType.JSON)
                .baseUri("http://localhost").port(port).basePath("/api/v1/users")
                .body(givenNewUser)
                .when().post()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", equalTo("User with username [user] already exists"));
    }

    @Test
    public void givenUsername_whenFindByUsername_thenReturnUserDto() {
        var givenAuthUsername = "admin";
        var givenAuthPassword = "password";
        var givenUsername = "user";

        given()
                .auth().basic(givenAuthUsername, givenAuthPassword)
                .baseUri("http://localhost").port(port)
                .basePath("/api/v1/users/{username}").pathParam("username", givenUsername)
                .when().get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("username", equalTo(givenUsername))
                .body("roles.size()", is(1))
                .body("roles", hasItems("USER"));
    }

    @Test
    public void givenInvalidUsername_whenFindByUsername_thenReturnNotFoundError() {
        var givenAuthUsername = "admin";
        var givenAuthPassword = "password";
        var givenUsername = "invalid";

        given()
                .auth().basic(givenAuthUsername, givenAuthPassword)
                .baseUri("http://localhost").port(port)
                .basePath("/api/v1/users/{username}").pathParam("username", givenUsername)
                .when().get()
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", equalTo("User with username [invalid] not found"));
    }
}
