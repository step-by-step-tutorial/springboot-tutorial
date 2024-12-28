package com.tutorial.springboot.security_rbac_jwt.api;

import com.tutorial.springboot.security_rbac_jwt.dto.PermissionDto;
import com.tutorial.springboot.security_rbac_jwt.fixture.permission.PermissionEntityAssertionUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.tutorial.springboot.security_rbac_jwt.fixture.permission.PermissionDtoFixture.newGivenPermission;
import static com.tutorial.springboot.security_rbac_jwt.fixture.permission.PermissionEntityFixture.findPermissionById;
import static com.tutorial.springboot.security_rbac_jwt.fixture.permission.PermissionEntityFixture.persistedGivenPermission;
import static com.tutorial.springboot.security_rbac_jwt.testutils.TestAuthenticationHelper.login;
import static com.tutorial.springboot.security_rbac_jwt.testutils.TestConstant.TEST_HOSTNAME;
import static com.tutorial.springboot.security_rbac_jwt.testutils.TestConstant.TEST_PROTOCOL;
import static com.tutorial.springboot.security_rbac_jwt.testutils.TestTokenUtils.requestToGetNewToken;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles({"test", "h2"})
public class PermissionApiTest {

    private static final String BASE_PATH = "/api/v1/permissions";

    @LocalServerPort
    private int port;

    @Autowired
    private EntityManagerFactory assistant;

    @BeforeEach
    void setUp() {
        login();
    }

    @Nested
    class SaveOneTests {

        @Test
        void whenSaveValidPermissionDto_thenReturnsIdAndStatusCreated() {
            var givenToken = requestToGetNewToken(port);
            var givenBody = newGivenPermission();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port).basePath(BASE_PATH)
                    .body(givenBody)
                    .when().post()
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .header("Location", containsString(BASE_PATH))
                    .body("", notNullValue());
        }
    }

    @Nested
    class SaveBatchTests {

        @Test
        void whenSaveValidPermissionDtoList_thenReturnIdListAndStatusCreated() {
            var givenToken = requestToGetNewToken(port);
            var givenBody = List.of(newGivenPermission("read"), newGivenPermission("write"));

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port).basePath(BASE_PATH + "/batch")
                    .body(givenBody)
                    .when().post()
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("size()", greaterThan(0));
        }
    }

    @Nested
    class FindTests {

        @Test
        void whenFindPermissionById_thenReturnsPermissionDtoAndStatusOk() {
            var givenToken = requestToGetNewToken(port);
            var permission = persistedGivenPermission(assistant);
            var givenId = permission.getId();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port).basePath(BASE_PATH + "/{id}")
                    .pathParam("id", givenId)
                    .when().get()
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(givenId.intValue()))
                    .body("name", equalTo(permission.getName()));
        }

        /**
         * Two permissions inserted into database through {@code data.sql} file
         * before loading application context.
         */
        @Test
        void whenFindAllPermissions_thenReturnsPermissionDtoListAndStatusOk() {
            var givenToken = requestToGetNewToken(port);

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port).basePath(BASE_PATH)
                    .when().get()
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("size()", is(2));
        }

        /**
         * Two permissions inserted into database through {@code data.sql} file
         * before loading application context.
         */
        @Test
        void whenFindPermissionsByPageAndSize_thenReturnsPagedPermissionDtoListAndStatusOk() {
            var givenToken = requestToGetNewToken(port);

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port).basePath(BASE_PATH + "/batch/{page}/{size}")
                    .pathParam("page", 0)
                    .pathParam("size", 10)
                    .when().get()
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("content.size()", is(2));
        }
    }

    @Nested
    class UpdateTests {

        @Test
        void whenUpdateValidPermissionDto_thenReturnsStatusNoContent() {
            var givenToken = requestToGetNewToken(port);
            var givenId = persistedGivenPermission(assistant).getId();
            var givenBody = newGivenPermission("updated_value").setId(givenId);

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port)
                    .basePath("/api/v1/permissions/{id}").pathParam("id", givenId)
                    .body(givenBody)
                    .when().put()
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value())
                    .body(is(emptyString()));

            var actual = findPermissionById(assistant, givenId);
            assertNotNull(actual);
            PermissionEntityAssertionUtils.assertPermission(actual, 1, 1);
            assertEquals("updated_value", actual.getName());
        }
    }

    @Nested
    class DeleteTests {

        @Test
        void whenDeletePermissionById_thenReturnsStatusNoContent() {
            var givenToken = requestToGetNewToken(port);
            var givenId = persistedGivenPermission(assistant).getId();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port)
                    .basePath("/api/v1/permissions/{id}").pathParam("id", givenId)
                    .when().delete()
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value())
                    .body(is(emptyString()));

            var actual = findPermissionById(assistant, givenId);
            assertNull(actual);
        }

        @Test
        void whenDeletePermissionBatchByIdList_thenReturnsStatusNoContent() {
            var givenToken = requestToGetNewToken(port);
            var givenId = persistedGivenPermission(assistant).getId();
            var givenBody = List.of(givenId);

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port).basePath("/api/v1/permissions/batch")
                    .body(givenBody)
                    .when().delete()
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value())
                    .body(is(emptyString()));

            var actual = findPermissionById(assistant, givenId);
            assertNull(actual);
        }

        @Test
        void whenDeleteAllPermissions_thenReturnsStatusNoContentAndClearsDatabase() {
            var givenToken = requestToGetNewToken(port);
            var givenId = persistedGivenPermission(assistant).getId();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port).basePath(BASE_PATH)
                    .when().delete()
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value())
                    .body(is(emptyString()));

            var actual = findPermissionById(assistant, givenId);
            assertNull(actual);
        }
    }

    @Nested
    class ExistsTests {

        @Test
        void whenPermissionExistsById_thenReturnsStatusOk() {
            var givenToken = requestToGetNewToken(port);
            var givenId = persistedGivenPermission(assistant).getId();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port)
                    .basePath("/api/v1/permissions/{id}").pathParam("id", givenId)
                    .when().head()
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body(is(emptyString()));
        }

        @Test
        void whenPermissionDoesNotExistById_thenReturnsStatusNotFound() {
            var givenToken = requestToGetNewToken(port);
            var givenId = -1;

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port)
                    .basePath("/api/v1/permissions/{id}").pathParam("id", givenId)
                    .when().head()
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body(is(emptyString()));
        }
    }

    @Nested
    class ValidateTests {

        @Test
        void whenSaveInvalidPermissionDto_thenReturnsErrorAndStatusBadRequest() {
            var givenToken = requestToGetNewToken(port);
            var givenBody = new PermissionDto();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port).basePath(BASE_PATH)
                    .body(givenBody)
                    .when().post()
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("errors.size()", is(1))
                    .body("errors", hasItem("name should not be blank"));
        }

        @Test
        void whenSaveInvalidPermissionDtoList_thenReturnErrorListAndStatusBadRequest() {
            var givenToken = requestToGetNewToken(port);
            var givenBody = List.of(new PermissionDto(), new PermissionDto());

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port).basePath(BASE_PATH + "/batch")
                    .body(givenBody)
                    .when().post()
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("errors.size()", is(2))
                    .body("errors", hasItem("name should not be blank"));
        }
    }

}
