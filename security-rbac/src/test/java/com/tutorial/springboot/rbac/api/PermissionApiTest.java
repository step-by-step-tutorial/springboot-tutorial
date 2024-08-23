package com.tutorial.springboot.rbac.api;

import com.tutorial.springboot.rbac.dto.PermissionDto;
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

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test database initialized by data.sql
 * Username: admin
 * Password: admin
 * */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class PermissionApiTest {

    @Autowired
    TestDatabaseAssistant testDatabaseAssistant;

    @LocalServerPort
    int port;

    @Nested
    class SaveTests {


        @Test
        void givenDto_whenSaveOne_thenReturnIdWithCreatedStatus() {
            var givenDto = DtoStubFactory.createPermission(1).asOne();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .auth().form("admin", "admin")
                    .baseUri("http://localhost").port(port).basePath("/api/v1/permissions")
                    .body(givenDto)
                    .when().post()
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .header("Location", containsString("/api/v1/permissions/"))
                    .body("", notNullValue());
        }

        @Test
        void givenDtoList_whenSaveBatch_thenReturnListOfIdWithCreatedStatus() {
            var numberOfPermissions = 2;
            var givenDtoList = DtoStubFactory.createPermission(numberOfPermissions).asList();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .auth().form("admin", "admin")
                    .baseUri("http://localhost").port(port).basePath("/api/v1/permissions/batch")
                    .body(givenDtoList)
                    .when().post()
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("size()", is(numberOfPermissions));
        }

    }

    @Nested
    class FindTests {

        @Test
        void givenId_whenFindOne_thenReturnDtoWithOKStatus() {
            var givenDto = testDatabaseAssistant.insertTestPermission(1).dto().asOne();
            var givenId = givenDto.getId();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .auth().form("admin", "admin")
                    .baseUri("http://localhost").port(port)
                    .basePath("/api/v1/permissions/{id}").pathParam("id", givenId)
                    .when().get()
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(givenId.intValue()))
                    .body("name", equalTo(givenDto.getName()));
        }

        @Test
        void givenNothing_whenFindAll_thenReturnListOfDtoWithOKStatus() {
            var expectedPermissionNumber = 2;

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .auth().form("admin", "admin")
                    .baseUri("http://localhost").port(port).basePath("/api/v1/permissions")
                    .when().get()
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("size()", is(expectedPermissionNumber));
        }
    }

    @Nested
    class UpdateTests {

        @Test
        void givenUpdatedDto_whenUpdate_thenReturnNoContentStatus() {
            var givenDto = testDatabaseAssistant.insertTestPermission(1)
                    .dto()
                    .asOne()
                    .setName("updated_value");
            var givenId = givenDto.getId();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .auth().form("admin", "admin")
                    .baseUri("http://localhost").port(port)
                    .basePath("/api/v1/permissions/{id}").pathParam("id", givenId)
                    .body(givenDto)
                    .when().put()
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value())
                    .body(is(emptyString()));

            var actual = testDatabaseAssistant.selectTestPermissions().dto().asOne();
            assertNotNull(actual);
            assertEquals("updated_value", actual.getName());
        }
    }

    @Nested
    class DeleteTests {

        @Test
        void givenId_whenDeleteOne_thenReturnNoContentStatus() {
            var givenId = testDatabaseAssistant.insertTestPermission(1).dto().asOne().getId();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .auth().form("admin", "admin")
                    .baseUri("http://localhost").port(port)
                    .basePath("/api/v1/permissions/{id}").pathParam("id", givenId)
                    .when().delete()
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value())
                    .body(is(emptyString()));

            var actual = testDatabaseAssistant.selectTestPermissions().dto().asOne();
            assertNull(actual);
        }

        @Test
        void givenListOfId_whenDeleteBatch_thenReturnNoContentStatus() {
            var givenIds = testDatabaseAssistant.insertTestPermission(2)
                    .dto()
                    .asList()
                    .stream()
                    .map(PermissionDto::getId)
                    .toList();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .auth().form("admin", "admin")
                    .baseUri("http://localhost").port(port).basePath("/api/v1/permissions/batch")
                    .body(givenIds)
                    .when().delete()
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value())
                    .body(is(emptyString()));

            var actual = testDatabaseAssistant.selectTestPermissions().dto().asList();
            assertTrue(actual.isEmpty());
        }

        @Test
        void givenNothing_whenDeleteAll_thenDeleteEveryThingWithNoContentStatus() {
            testDatabaseAssistant.insertTestPermission(2);

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .auth().form("admin", "admin")
                    .baseUri("http://localhost").port(port).basePath("/api/v1/permissions")
                    .when().delete()
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value())
                    .body(is(emptyString()));

            var actual = testDatabaseAssistant.selectTestPermissions().dto().asList();
            assertTrue(actual.isEmpty());
        }
    }

    @Nested
    class ExistsTests {

        @Test
        void givenId_whenExists_ThenReturnOKStatus() {
            var givenId = testDatabaseAssistant.insertTestPermission(1).dto().asOne().getId();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .baseUri("http://localhost").port(port)
                    .basePath("/api/v1/permissions/{id}").pathParam("id", givenId)
                    .when().head()
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body(is(emptyString()));
        }

        @Test
        void givenId_whenNotExist_ThenReturnNotFoundStatus() {
            var givenId = -1;

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .baseUri("http://localhost").port(port)
                    .basePath("/api/v1/permissions/{id}").pathParam("id", givenId)
                    .when().head()
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body(is(emptyString()));
        }
    }
}
