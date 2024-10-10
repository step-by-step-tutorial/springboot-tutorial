package com.tutorial.springboot.security_rbac_jwt.test_utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

public final class SecurityTestUtils {

    private SecurityTestUtils() {
    }

    public static String requestToGetToken() {
        var hostname = "localhost";
        var port = 8080;
        var username = "admin";
        var password = "admin";
        return requestToGetToken(hostname, port, username, password);
    }

    private static String requestToGetToken(String hostname, int port, String username, String password) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .auth().basic(username, password)
                .baseUri("http://" + hostname).port(port).basePath("/api/v1/token/new")
                .when().get()
                .andReturn().body().jsonPath().getString("token");
    }

    public static void authenticateToTestEnv() {
        authenticateToTestEnv("admin", "admin");
    }

    public static void authenticateToTestEnv(String username, String password) {
        var auth = new UsernamePasswordAuthenticationToken(username, password);
        SecurityContextHolder.setContext(new SecurityContextImpl(auth));
    }
}
