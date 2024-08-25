package com.tutorial.springboot.rbac.api;

import com.tutorial.springboot.rbac.dto.UserDto;
import com.tutorial.springboot.rbac.test_utils.stub.DtoStubFactory;
import com.tutorial.springboot.rbac.test_utils.stub.TestDatabaseAssistant;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static com.tutorial.springboot.rbac.test_utils.SecurityTestUtils.getTestToken;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test database initialized by data.sql
 * Username: admin
 * Password: admin
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class UserApiTest {

    @Autowired
    TestDatabaseAssistant testDatabaseAssistant;

    @LocalServerPort
    int port;

    @Nested
    class SaveTests {


        @Test
        void givenDto_whenSaveOne_thenReturnIdWithCreatedStatus() {
            var givenToken = getTestToken();
            var givenDto = DtoStubFactory.createUser(1, 1, 1).asOne();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri("http://localhost").port(port).basePath("/api/v1/users")
                    .body(givenDto)
                    .when().post()
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .header("Location", containsString("/api/v1/users/"))
                    .body("", notNullValue());
        }

        @Test
        void givenDtoList_whenSaveBatch_thenReturnListOfIdWithCreatedStatus() {
            var givenToken = getTestToken();
            var numberOfUsers = 2;
            var givenDtoList = DtoStubFactory.createUser(numberOfUsers, 1, 1).asList();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri("http://localhost").port(port).basePath("/api/v1/users/batch")
                    .body(givenDtoList)
                    .when().post()
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("size()", is(numberOfUsers));
        }

    }

    @Nested
    class FindTests {

        @Test
        void givenId_whenFindOne_thenReturnDtoWithOKStatus() {
            var givenToken = getTestToken();
            var givenDto = testDatabaseAssistant.insertTestUser(1, 1, 1).dto().asOne();
            var givenId = givenDto.getId();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri("http://localhost").port(port)
                    .basePath("/api/v1/users/{id}").pathParam("id", givenId)
                    .when().get()
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(givenId.intValue()))
                    .body("username", equalTo(givenDto.getUsername()))
                    .body("email", equalTo(givenDto.getEmail()));
        }

        @Test
        void givenNothing_whenFindAll_thenReturnListOfDtoWithOKStatus() {
            var givenToken = getTestToken();
            var expectedUserNumber = 2;

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri("http://localhost").port(port).basePath("/api/v1/users")
                    .when().get()
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("size()", is(expectedUserNumber));
        }
    }

    @Nested
    class UpdateTests {

        @Test
        void givenUpdatedDto_whenUpdate_thenReturnNoContentStatus() {
            var givenToken = getTestToken();
            var givenDto = testDatabaseAssistant.insertTestUser(1, 1, 1)
                    .dto()
                    .asOne()
                    .setUsername("newusername")
                    .setPassword("newpassword")
                    .setEmail("newusername@host.com");
            var givenId = givenDto.getId();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri("http://localhost").port(port)
                    .basePath("/api/v1/users/{id}").pathParam("id", givenId)
                    .body(givenDto)
                    .when().put()
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value())
                    .body(is(emptyString()));

            var actual = testDatabaseAssistant.selectTestUser().entity().asOne();
            assertNotNull(actual);
            assertEquals("newusername", actual.getUsername());
            assertEquals("newpassword", actual.getPassword());
            assertEquals("newusername@host.com", actual.getEmail());
            assertTrue(actual.isEnabled());
        }
    }

    @Nested
    class DeleteTests {

        @Test
        void givenId_whenDeleteOne_thenReturnNoContentStatus() {
            var givenToken = getTestToken();
            var givenId = testDatabaseAssistant.insertTestUser(1, 1, 1).dto().asOne().getId();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri("http://localhost").port(port)
                    .basePath("/api/v1/users/{id}").pathParam("id", givenId)
                    .when().delete()
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value())
                    .body(is(emptyString()));

            var actual = testDatabaseAssistant.selectTestUser().dto().asOne();
            assertNull(actual);
        }

        @Test
        void givenListOfId_whenDeleteBatch_thenReturnNoContentStatus() {
            var givenToken = getTestToken();
            var givenIds = testDatabaseAssistant.insertTestUser(2, 1, 1)
                    .dto()
                    .asList()
                    .stream()
                    .map(UserDto::getId)
                    .toList();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri("http://localhost").port(port).basePath("/api/v1/users/batch")
                    .body(givenIds)
                    .when().delete()
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value())
                    .body(is(emptyString()));

            var actual = testDatabaseAssistant.selectTestUser().dto().asList();
            assertTrue(actual.isEmpty());
        }

        @Test
        void givenNothing_whenDeleteAll_thenDeleteEveryThingWithNoContentStatus() {
            var givenToken = getTestToken();
            testDatabaseAssistant.insertTestUser(2, 1, 1);

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri("http://localhost").port(port).basePath("/api/v1/users")
                    .when().delete()
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value())
                    .body(is(emptyString()));

            var actual = testDatabaseAssistant.selectTestUser().dto().asList();
            assertTrue(actual.isEmpty());
        }
    }

    @Nested
    class ExistsTests {

        @Test
        void givenId_whenExists_ThenReturnOKStatus() {
            var givenToken = getTestToken();
            var givenId = testDatabaseAssistant.insertTestUser(1, 1, 1).dto().asOne().getId();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri("http://localhost").port(port)
                    .basePath("/api/v1/users/{id}").pathParam("id", givenId)
                    .when().head()
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body(is(emptyString()));
        }

        @Test
        void givenId_whenNotExist_ThenReturnNotFoundStatus() {
            var givenToken = getTestToken();
            var givenId = -1;

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri("http://localhost").port(port)
                    .basePath("/api/v1/users/{id}").pathParam("id", givenId)
                    .when().head()
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body(is(emptyString()));
        }
    }
}
