package com.tutorial.springboot.rbac.api;

import com.tutorial.springboot.rbac.dto.PermissionDto;
import com.tutorial.springboot.rbac.fixture.DtoStubFactory;
import com.tutorial.springboot.rbac.fixture.TestDatabaseAssistant;
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

import static com.tutorial.springboot.rbac.fixture.StubFactory.TEST_PERMISSION_NAME;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

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
        void givenDtoWhenSaveThenReturnId() {
            var givenDto = new DtoStubFactory()
                    .addPermission()
                    .get()
                    .asOne();

            RestAssured.given()
                    .auth()
                    .form("admin", "admin")
                    .contentType(ContentType.JSON)
                    .baseUri("http://localhost")
                    .port(port)
                    .basePath("/api/v1/permissions")
                    .body(givenDto)
                    .when()
                    .post()
                    .then()
                    .statusCode(HttpStatus.CREATED.value());
        }

        @Test
        void givenDtoListWhenSaveThenReturnIdList() {
            var givenList = new DtoStubFactory()
                    .addPermission()
                    .get()
                    .asList();

            RestAssured.given()
                    .auth()
                    .form("admin", "admin")
                    .contentType(ContentType.JSON)
                    .baseUri("http://localhost")
                    .port(port)
                    .basePath("/api/v1/permissions/batch")
                    .body(givenList)
                    .when()
                    .post()
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("size()", is(1));
        }
    }

    @Nested
    class FindTests {

        @Test
        void givenIdWhenFindThenReturnDto() {
            var givenDto = testDatabaseAssistant.newTestPermission().asDto;
            var givenId = givenDto.getId();

            RestAssured.given()
                    .auth()
                    .form("admin", "admin")
                    .contentType(ContentType.JSON)
                    .baseUri("http://localhost")
                    .port(port)
                    .basePath("/api/v1/permissions/{id}")
                    .pathParam("id", givenId)
                    .get()
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(givenId.intValue()))
                    .body("name", equalTo(givenDto.getName()));
        }

        @Test
        void whenFindAllThenReturnListOfPermissions() {
            RestAssured.given()
                    .auth()
                    .form("admin", "admin")
                    .contentType(ContentType.JSON)
                    .baseUri("http://localhost")
                    .port(port)
                    .basePath("/api/v1/permissions")
                    .when()
                    .get()
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("size()", is(2));
        }
    }

    @Nested
    class UpdateTests {

        @Test
        void givenUpdatedDtoWhenUpdateThenRunSuccessfully() {
            var givenDto = testDatabaseAssistant.newTestPermission()
                    .asDto
                    .setName("UPDATED_PRIVILEGE");
            var givenId = givenDto.getId();

            RestAssured.given()
                    .auth()
                    .form("admin", "admin")
                    .contentType(ContentType.JSON)
                    .baseUri("http://localhost")
                    .port(port)
                    .basePath("/api/v1/permissions/{id}")
                    .pathParam("id", givenId)
                    .body(givenDto)
                    .when()
                    .put()
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            var actual = testDatabaseAssistant.fetchTestPermission().asDto;
            assertNotNull(actual);
            assertEquals("UPDATED_PRIVILEGE", actual.getName());
        }
    }

    @Nested
    class DeleteTests {

        @Test
        void givenIdWhenDeleteThenRunSuccessfully() {
            var givenId = testDatabaseAssistant.newTestPermission().asDto.getId();

            RestAssured.given()
                    .auth()
                    .form("admin", "admin")
                    .contentType(ContentType.JSON)
                    .baseUri("http://localhost")
                    .port(port)
                    .basePath("/api/v1/permissions/{id}")
                    .pathParam("id", givenId)
                    .when()
                    .delete()
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            var actual = testDatabaseAssistant.fetchTestPermission().asDto;
            assertNull(actual);
        }

        @Test
        void givenIdListWhenDeleteThenRunSuccessfully() {
            var givenIds = testDatabaseAssistant.newTestPermission(2)
                    .asDto
                    .stream()
                    .map(PermissionDto::getId)
                    .toList();

            RestAssured.given()
                    .auth()
                    .form("admin", "admin")
                    .contentType(ContentType.JSON)
                    .baseUri("http://localhost")
                    .port(port)
                    .basePath("/api/v1/permissions/batch")
                    .body(givenIds)
                    .when()
                    .delete()
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            var actual = testDatabaseAssistant.fetchTestPermission().asDto;
            assertNull(actual);
        }

        @Test
        void givenNothingWhenDeleteThenDeleteEveryThing() {
            var givenIds = testDatabaseAssistant.newTestPermission(2)
                    .asDto
                    .stream()
                    .map(PermissionDto::getId)
                    .toList();

            RestAssured.given()
                    .auth()
                    .form("admin", "admin")
                    .contentType(ContentType.JSON)
                    .baseUri("http://localhost")
                    .port(port)
                    .basePath("/api/v1/permissions")
                    .when()
                    .delete()
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            var actual = testDatabaseAssistant.fetchTestPermission().asDto;
            assertNull(actual);
        }
    }

    @Nested
    class ExistsTests {

        @Test
        void givenIdWhenCheckExistenceThenReturnAppropriateStatus() {
            var givenId = testDatabaseAssistant.newTestPermission().asDto.getId();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .baseUri("http://localhost")
                    .port(port)
                    .basePath("/api/v1/permissions/{id}")
                    .pathParam("id", givenId)
                    .when()
                    .head()
                    .then()
                    .statusCode(HttpStatus.OK.value());
        }
    }
}