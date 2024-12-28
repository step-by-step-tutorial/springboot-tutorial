package com.tutorial.springboot.security_rbac_jwt.api;

import com.tutorial.springboot.security_rbac_jwt.dto.RoleDto;
import com.tutorial.springboot.security_rbac_jwt.entity.Role;
import com.tutorial.springboot.security_rbac_jwt.fixture.role.RoleDtoAssertionUtils;
import com.tutorial.springboot.security_rbac_jwt.fixture.role.RoleEntityAssertionUtils;
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

import static com.tutorial.springboot.security_rbac_jwt.fixture.role.RoleDtoFixture.newGivenRole;
import static com.tutorial.springboot.security_rbac_jwt.fixture.role.RoleEntityFixture.findRoleById;
import static com.tutorial.springboot.security_rbac_jwt.fixture.role.RoleEntityFixture.persistedGivenRole;
import static com.tutorial.springboot.security_rbac_jwt.testutils.TestAuthenticationHelper.login;
import static com.tutorial.springboot.security_rbac_jwt.testutils.TestConstant.TEST_HOSTNAME;
import static com.tutorial.springboot.security_rbac_jwt.testutils.TestConstant.TEST_PROTOCOL;
import static com.tutorial.springboot.security_rbac_jwt.testutils.TestTokenUtils.requestToGetNewToken;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles({"test", "h2"})
public class RoleApiTest {

    private static final String BASE_PATH = "/api/v1/roles";

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
        void whenRoleIsValid_thenSaveAndReturnIdWithCreatedStatus() {
            var givenToken = requestToGetNewToken(port);
            var givenBody = newGivenRole();

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
        void whenRoleListIsValid_thenSaveAndReturnListOfIdsWithCreatedStatus() {
            var givenToken = requestToGetNewToken(port);
            var givenBody = List.of(newGivenRole("guest"), newGivenRole("host"));

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port).basePath(BASE_PATH + "/batch")
                    .body(givenBody)
                    .when().post()
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("size()", is(2));
        }
    }

    @Nested
    class FindTests {

        @Test
        void whenIdExists_thenReturnRoleWithOkStatus() {
            var givenToken = requestToGetNewToken(port);
            var role = persistedGivenRole(assistant);
            var givenId = role.getId();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port).basePath(BASE_PATH + "/{id}")
                    .pathParam("id", givenId)
                    .when().get()
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(givenId.intValue()))
                    .body("name", equalTo(role.getName()));
        }

        @Test
        void whenNoIdsProvided_thenReturnAllRolesWithOkStatus() {
            var givenToken = requestToGetNewToken(port);

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port).basePath(BASE_PATH)
                    .when().get()
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("size()", is(3));
        }

        @Test
        void whenPagedRequestIsValid_thenReturnPagedListOfRolesWithOkStatus() {
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
                    .body("content.size()", is(3));
        }
    }

    @Nested
    class UpdateTests {

        @Test
        void whenUpdatedRoleIsProvided_thenUpdateAndReturnNoContentStatus() {
            var givenToken = requestToGetNewToken(port);
            var givenId = persistedGivenRole(assistant).getId();
            var givenBody = newGivenRole("updated_value");
            givenBody.setId(givenId);

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port).basePath(BASE_PATH + "/{id}")
                    .pathParam("id", givenId)
                    .body(givenBody)
                    .when().put()
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value())
                    .body(is(emptyString()));

            var actual = findRoleById(assistant, givenId);

            RoleEntityAssertionUtils.assertRole(actual, 1, 1);
            assertEquals("updated_value", actual.getName());
        }
    }

    @Nested
    class DeleteTests {

        @Test
        void whenIdExists_thenDeleteRoleAndReturnNoContentStatus() {
            var givenToken = requestToGetNewToken(port);
            var givenId = persistedGivenRole(assistant).getId();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port).basePath(BASE_PATH + "/{id}")
                    .pathParam("id", givenId)
                    .when().delete()
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value())
                    .body(is(emptyString()));

            var actual = findRoleById(assistant, givenId);
            assertNull(actual);
        }

        @Test
        void whenValidListOfIdsProvided_thenDeleteRolesAndReturnNoContentStatus() {
            var givenToken = requestToGetNewToken(port);
            var givenId = persistedGivenRole(assistant).getId();
            var givenBody = List.of(givenId);

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port).basePath(BASE_PATH + "/batch")
                    .body(givenBody)
                    .when().delete()
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value())
                    .body(is(emptyString()));

            var actual = findRoleById(assistant, givenId);

            assertNull(actual);
        }

        @Test
        void whenNoDataProvided_thenDeleteAllRolesAndReturnNoContentStatus() {
            var givenToken = requestToGetNewToken(port);
            var givenId = persistedGivenRole(assistant).getId();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port).basePath(BASE_PATH)
                    .when().delete()
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value())
                    .body(is(emptyString()));

            var actual = findRoleById(assistant, givenId);

            assertNull(actual);
        }
    }

    @Nested
    class ExistsTests {

        @Test
        void whenIdExists_thenReturnOkStatus() {
            var givenToken = requestToGetNewToken(port);
            var givenId = persistedGivenRole(assistant).getId();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port).basePath(BASE_PATH + "/{id}")
                    .pathParam("id", givenId)
                    .when().head()
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body(is(emptyString()));
        }

        @Test
        void whenIdDoesNotExist_thenReturnNotFoundStatus() {
            var givenToken = requestToGetNewToken(port);
            var givenId = -1;

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port).basePath(BASE_PATH + "/{id}")
                    .pathParam("id", givenId)
                    .when().head()
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body(is(emptyString()));
        }
    }

    @Nested
    class ValidateTests {

        @Test
        void whenRoleIsInvalid_thenReturnBadRequestWithValidationError() {
            var givenToken = requestToGetNewToken(port);
            var givenBody = new RoleDto();

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
        void whenRoleListIsInvalid_thenReturnBadRequestWithListOfErrors() {
            var givenToken = requestToGetNewToken(port);
            var givenBody = List.of(new RoleDto(), new RoleDto());

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
