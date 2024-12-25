package com.tutorial.springboot.security_rbac_jwt.api;

import com.tutorial.springboot.security_rbac_jwt.dto.UserDto;
import com.tutorial.springboot.security_rbac_jwt.entity.User;
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

import static com.tutorial.springboot.security_rbac_jwt.testutils.DtoFixture.newGivenUser;
import static com.tutorial.springboot.security_rbac_jwt.testutils.TestConstant.TEST_HOSTNAME;
import static com.tutorial.springboot.security_rbac_jwt.testutils.TestConstant.TEST_PROTOCOL;
import static com.tutorial.springboot.security_rbac_jwt.testutils.TestTokenUtils.requestToGetNewToken;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles({"test", "h2"})
public class UserApiTest {

    private static final String BASE_PATH = "/api/v1/users";

    @LocalServerPort
    private int port;

    @Autowired
    private EntityManagerFactory assistant;

    private User insertUser() {
        var em = assistant.createEntityManager();
        var transaction = em.getTransaction();

        transaction.begin();
        var user = EntityFixture.newGivenUser();
        em.persist(user);
        em.flush();
        em.clear();
        transaction.commit();

        return user;
    }

    @Nested
    class SaveOneTests {

        @Test
        void givenUser_whenSave_thenReturnIdWithCreatedStatus() {
            var givenToken = requestToGetNewToken(port);
            var givenBody = newGivenUser();

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


        @Test
        void givenInvalidUser_whenSave_thenReturnErrorWithBadRequestStatus() {
            var givenToken = requestToGetNewToken(port);
            var givenBody = new UserDto();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port).basePath(BASE_PATH)
                    .body(givenBody)
                    .when().post()
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("errors.size()", is(3))
                    .body("errors", hasItems("username should not be blank", "password should not be blank", "email should not be blank"));
        }

        @Test
        void givenInvalidUserList_whenSaveBatch_thenReturnListOfErrorsWithBadRequestStatus() {
            var givenToken = requestToGetNewToken(port);
            var givenBody = List.of(new UserDto(), new UserDto());

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port).basePath(BASE_PATH + "/batch")
                    .body(givenBody)
                    .when().post()
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("errors.size()", is(6))
                    .body("errors", hasItems("username should not be blank", "password should not be blank", "email should not be blank"));
        }
    }

    @Nested
    class SaveBatchTests {
        @Test
        void givenUserList_whenSave_thenReturnListOfIdWithCreatedStatus() {
            var givenToken = requestToGetNewToken(port);
            var givenBody = List.of(newGivenUser("Bob"), newGivenUser("Alice"));

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
        void givenId_whenFindOne_thenReturnUserWithOKStatus() {
            var givenToken = requestToGetNewToken(port);
            var givenUser = insertUser();
            var givenId = givenUser.getId();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port)
                    .basePath(BASE_PATH + "/{id}").pathParam("id", givenId)
                    .when().get()
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(givenId.intValue()))
                    .body("username", equalTo(givenUser.getUsername()))
                    .body("email", equalTo(givenUser.getEmail()));
        }

        @Test
        void givenNothing_whenFindAll_thenReturnListOfUserWithOKStatus() {
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
        void givenPageAndSize_whenFindBatch_thenReturnListOfUserWithOkStatus() {
            var givenToken = requestToGetNewToken(port);

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port)
                    .basePath(BASE_PATH + "/batch/{page}/{size}")
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
        void givenUpdatedUser_whenUpdate_thenReturnNoContentStatus() {
            var givenToken = requestToGetNewToken(port);
            var user = insertUser();
            var givenId = user.getId();
            var givenBody = newGivenUser()
                    .setId(givenId)
                    .setUsername("newusername")
                    .setEmail("newusername@email.com");

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port)
                    .basePath(BASE_PATH + "/{id}").pathParam("id", givenId)
                    .body(givenBody)
                    .when().put()
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value())
                    .body(is(emptyString()));

            var em = assistant.createEntityManager();
            var transaction = em.getTransaction();
            transaction.begin();
            var actual = em.find(User.class, givenId);
            transaction.commit();

            assertNotNull(actual);
            assertEquals("newusername", actual.getUsername());
            assertEquals("newusername@email.com", actual.getEmail());
            assertTrue(actual.isEnabled());
        }
    }

    @Nested
    class DeleteTests {

        @Test
        void givenId_whenDeleteOne_thenReturnNoContentStatus() {
            var givenToken = requestToGetNewToken(port);
            var user = insertUser();
            var givenId = user.getId();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port)
                    .basePath(BASE_PATH + "/{id}").pathParam("id", givenId)
                    .when().delete()
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value())
                    .body(is(emptyString()));

            var em = assistant.createEntityManager();
            var transaction = em.getTransaction();
            transaction.begin();
            var actual = em.find(User.class, givenId);
            transaction.commit();

            assertNull(actual);
        }

        @Test
        void givenListOfId_whenDeleteBatch_thenReturnNoContentStatus() {
            var givenToken = requestToGetNewToken(port);
            var user = insertUser();
            var givenId = user.getId();
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

            var em = assistant.createEntityManager();
            var transaction = em.getTransaction();
            transaction.begin();
            var actual = em.find(User.class, givenId);
            transaction.commit();

            assertNull(actual);
        }

        @Test
        void givenNothing_whenDeleteAll_thenDeleteEveryThingWithNoContentStatus() {
            var givenToken = requestToGetNewToken(port);
            var user = insertUser();
            var givenId = user.getId();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port).basePath(BASE_PATH)
                    .when().delete()
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value())
                    .body(is(emptyString()));

            var em = assistant.createEntityManager();
            var transaction = em.getTransaction();
            transaction.begin();
            var actual = em.find(User.class, givenId);
            transaction.commit();

            assertNull(actual);
        }
    }

    @Nested
    class ExistsTests {

        @Test
        void givenId_whenExists_ThenReturnOKStatus() {
            var givenToken = requestToGetNewToken(port);
            var user = insertUser();
            var givenId = user.getId();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port)
                    .basePath(BASE_PATH + "/{id}").pathParam("id", givenId)
                    .when().head()
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body(is(emptyString()));
        }

        @Test
        void givenId_whenNotExist_ThenReturnNotFoundStatus() {
            var givenToken = requestToGetNewToken(port);
            var givenId = -1;

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port)
                    .basePath(BASE_PATH + "/{id}").pathParam("id", givenId)
                    .when().head()
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body(is(emptyString()));
        }
    }
}
