package com.tutorial.springboot.security_rbac_jwt.api;

import com.tutorial.springboot.security_rbac_jwt.dto.PermissionDto;
import com.tutorial.springboot.security_rbac_jwt.entity.Permission;
import com.tutorial.springboot.security_rbac_jwt.testutils.EntityFixture;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.tutorial.springboot.security_rbac_jwt.testutils.DtoFixture.newGivenPermission;
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

    private Permission insertPermission() {
        var em = assistant.createEntityManager();
        var transaction = em.getTransaction();

        transaction.begin();
        var permission = EntityFixture.newGivenPermission();
        em.persist(permission);
        em.flush();
        em.clear();
        transaction.commit();

        return permission;
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
            var permission = insertPermission();
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

        @Test
        void whenFindAllPermissions_thenReturnsPermissionDtoListAndStatusOk() {
            var givenToken = requestToGetNewToken(port);
            var expectedPermissionNumber = 2;

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port).basePath(BASE_PATH)
                    .when().get()
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("size()", is(expectedPermissionNumber));
        }

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
            var permission = insertPermission();
            var givenToken = requestToGetNewToken(port);
            var givenBody = newGivenPermission("updated_value");
            givenBody.setId(permission.getId());
            var givenId = permission.getId();

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

            var em = assistant.createEntityManager();
            em.getTransaction().begin();
            var actual = em.find(Permission.class, givenId);
            em.getTransaction().commit();

            assertNotNull(actual);
            assertEquals("updated_value", actual.getName());
        }
    }

    @Nested
    class DeleteTests {

        @Test
        void whenDeletePermissionById_thenReturnsStatusNoContent() {
            var givenToken = requestToGetNewToken(port);
            var permission = insertPermission();
            var givenId = permission.getId();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port)
                    .basePath("/api/v1/permissions/{id}").pathParam("id", givenId)
                    .when().delete()
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value())
                    .body(is(emptyString()));

            var em = assistant.createEntityManager();
            em.getTransaction().begin();
            var actual = em.find(Permission.class, givenId);
            em.getTransaction().commit();

            assertNull(actual);
        }

        @Test
        void whenDeletePermissionBatchByIdList_thenReturnsStatusNoContent() {
            var permission = insertPermission();
            var givenToken = requestToGetNewToken(port);
            var givenId = permission.getId();
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

            var em = assistant.createEntityManager();
            em.getTransaction().begin();
            var actual = em.find(Permission.class, givenId);
            em.getTransaction().commit();

            assertNull(actual);
        }

        @Test
        void whenDeleteAllPermissions_thenReturnsStatusNoContentAndClearsDatabase() {
            var givenToken = requestToGetNewToken(port);
            var permission = insertPermission();
            var givenId = permission.getId();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port).basePath(BASE_PATH)
                    .when().delete()
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value())
                    .body(is(emptyString()));

            var em = assistant.createEntityManager();
            em.getTransaction().begin();
            var actual = em.find(Permission.class, givenId);
            em.getTransaction().commit();

            assertNull(actual);
        }
    }

    @Nested
    class ExistsTests {

        @Test
        void whenPermissionExistsById_thenReturnsStatusOk() {
            var givenToken = requestToGetNewToken(port);
            var permission = insertPermission();
            var givenId = permission.getId();

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
