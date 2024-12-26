package com.tutorial.springboot.security_rbac_jwt.api;

import com.tutorial.springboot.security_rbac_jwt.dto.ChangeCredentialsDto;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.tutorial.springboot.security_rbac_jwt.testutils.DtoFixture.newGivenUser;
import static com.tutorial.springboot.security_rbac_jwt.testutils.TestAuthenticationHelper.login;
import static com.tutorial.springboot.security_rbac_jwt.testutils.TestConstant.TEST_HOSTNAME;
import static com.tutorial.springboot.security_rbac_jwt.testutils.TestConstant.TEST_PROTOCOL;
import static com.tutorial.springboot.security_rbac_jwt.testutils.TestTokenUtils.requestToGetNewToken;
import static com.tutorial.springboot.security_rbac_jwt.testutils.TestTokenUtils.saveUserThroughApi;
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
        login();
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
        void whenSavingSingleUser_thenReturnsCreatedStatusAndId() {
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
    }

    @Nested
    class SaveBatchTests {
        @Test
        void whenSavingListOfUsers_thenReturnsCreatedStatusAndIds() {
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
        void whenFindingUserById_thenReturnsUserWithOkStatus() {
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
        void whenFindingAllUsers_thenReturnsUsersListWithOkStatus() {
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
        void whenFindingUsersByPageAndSize_thenReturnsPaginatedUsersList() {
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
        void whenUpdatingUser_thenReturnsNoContentStatus() {
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
        void whenDeletingUserById_thenReturnsNoContentStatus() {
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
        void whenDeletingListOfUserIds_thenReturnsNoContentStatus() {
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
        void whenDeletingAllUsers_thenDeletesEverythingAndReturnsNoContentStatus() {
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
        void whenCheckingIfUserExistsById_thenReturnsOkStatus() {
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
        void whenCheckingIfNonExistentUserExists_thenReturnsNotFoundStatus() {
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

    @Nested
    class ValidateTests {

        @Test
        void whenSavingInvalidUser_thenReturnsBadRequestWithErrors() {
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
        void whenSavingListOfInvalidUsers_thenReturnsBadRequestWithErrorsForEachUser() {
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
    class ChangePasswordTests {

        @Test
        void whenChangingPassword_thenReturnsOkStatus() {
            var user = newGivenUser();
            var userId = saveUserThroughApi(port, user);
            var givenToken = requestToGetNewToken(TEST_HOSTNAME, port, user.getUsername(), user.getPassword());
            var givenNewPassword = "updated_password";
            var givenBody = new ChangeCredentialsDto(user.getUsername(), user.getPassword(), givenNewPassword);

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri(TEST_PROTOCOL + TEST_HOSTNAME).port(port).basePath(BASE_PATH + "/change-password")
                    .body(givenBody)
                    .when().patch()
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value())
                    .body(is(emptyString()));

            var em = assistant.createEntityManager();
            var transaction = em.getTransaction();
            transaction.begin();
            var actual = em.find(User.class, userId);
            transaction.commit();

            assertNotNull(actual);

            var passwordEncoder = new BCryptPasswordEncoder();
            assertTrue(passwordEncoder.matches(givenNewPassword, actual.getPassword()));
            assertEquals(user.getUsername(), actual.getUsername());
            assertEquals(user.getEmail(), actual.getEmail());
        }
    }
}
