package com.tutorial.springboot.security_rbac_jwt.api;

import com.tutorial.springboot.security_rbac_jwt.dto.RoleDto;
import com.tutorial.springboot.security_rbac_jwt.testutils.TestTokenUtils;
import com.tutorial.springboot.security_rbac_jwt.testutils.stub.assistant.RoleTestAssistant;
import com.tutorial.springboot.security_rbac_jwt.testutils.stub.factory.RoleTestFactory;
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

import java.util.List;

import static com.tutorial.springboot.security_rbac_jwt.testutils.TestHttpUtils.TEST_HOSTNAME;
import static com.tutorial.springboot.security_rbac_jwt.testutils.TestTokenUtils.requestToGetNewToken;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles({"test", "h2"})
public class RoleApiTest {

    private static final String BASE_PATH = "/api/v1/roles";

    @LocalServerPort
    private int port;

    @Autowired
    private RoleTestAssistant assistant;

    @Autowired
    private RoleTestFactory factory;
    
    @Nested
    class SaveTests {

        @Test
        void givenDto_whenSaveOne_thenReturnIdWithCreatedStatus() {
            var givenToken = TestTokenUtils.requestToGetNewToken();
            var givenBody = factory.newInstances(1).dto().asOne();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri("http://" + TEST_HOSTNAME).port(port).basePath(BASE_PATH)
                    .body(givenBody)
                    .when().post()
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .header("Location", containsString(BASE_PATH))
                    .body("", notNullValue());
        }

        @Test
        void givenDtoList_whenSaveBatch_thenReturnListOfIdWithCreatedStatus() {
            var givenToken = TestTokenUtils.requestToGetNewToken();
            var numberOfRoles = 2;
            var givenBody = factory.newInstances(numberOfRoles).dto().asList();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri("http://" + TEST_HOSTNAME).port(port).basePath(BASE_PATH + "/batch")
                    .body(givenBody)
                    .when().post()
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("size()", is(numberOfRoles));
        }


        @Test
        void givenInvalidDto_whenSaveOne_thenReturnErrorWithBadRequestStatus() {
            var givenToken = TestTokenUtils.requestToGetNewToken();
            var givenBody = new RoleDto();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri("http://" + TEST_HOSTNAME).port(port).basePath(BASE_PATH)
                    .body(givenBody)
                    .when().post()
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("errors.size()", is(1))
                    .body("errors", hasItem("name should not be blank"));
        }

        @Test
        void givenInvalidDtoList_whenSaveBatch_thenReturnListOfErrorsWithBadRequestStatus() {
            var givenToken = TestTokenUtils.requestToGetNewToken();
            var givenBody = List.of(new RoleDto(), new RoleDto());

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri("http://" + TEST_HOSTNAME).port(port).basePath(BASE_PATH + "/batch")
                    .body(givenBody)
                    .when().post()
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("errors.size()", is(2))
                    .body("errors", hasItem("name should not be blank"));
        }

    }

    @Nested
    class FindTests {

        @Test
        void givenId_whenFindOne_thenReturnDtoWithOKStatus() {
            var givenToken = TestTokenUtils.requestToGetNewToken();
            var givenRole = assistant.populate(1).dto().asOne();
            var givenId = givenRole.getId();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri("http://" + TEST_HOSTNAME).port(port).basePath(BASE_PATH + "/{id}")
                    .pathParam("id", givenId)
                    .when().get()
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(givenId.intValue()))
                    .body("name", equalTo(givenRole.getName()));
        }

        @Test
        void givenNothing_whenFindAll_thenReturnListOfDtoWithOKStatus() {
            var givenToken = TestTokenUtils.requestToGetNewToken();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri("http://" + TEST_HOSTNAME).port(port).basePath(BASE_PATH)
                    .when().get()
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("size()", is(3));
        }

        @Test
        void givenPageAndSize_whenFindBatch_thenReturnListOfDtoWithOkStatus() {
            var givenToken = TestTokenUtils.requestToGetNewToken();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri("http://" + TEST_HOSTNAME).port(port).basePath(BASE_PATH + "/batch/{page}/{size}")
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
        void givenUpdatedDto_whenUpdate_thenReturnNoContentStatus() {
            var givenToken = TestTokenUtils.requestToGetNewToken();
            var givenBody = assistant.populate(1)
                    .dto()
                    .asOne()
                    .setName("updated_value");
            var givenId = givenBody.getId();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri("http://" + TEST_HOSTNAME).port(port).basePath(BASE_PATH + "/{id}")
                    .pathParam("id", givenId)
                    .body(givenBody)
                    .when().put()
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value())
                    .body(is(emptyString()));

            var actual = assistant.select().dto().asOne();
            assertNotNull(actual);
            assertEquals("updated_value", actual.getName());
        }
    }

    @Nested
    class DeleteTests {

        @Test
        void givenId_whenDeleteOne_thenReturnNoContentStatus() {
            var givenToken = TestTokenUtils.requestToGetNewToken();
            var givenId = assistant.populate(1).dto().asOne().getId();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri("http://" + TEST_HOSTNAME).port(port).basePath(BASE_PATH + "/{id}")
                    .pathParam("id", givenId)
                    .when().delete()
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value())
                    .body(is(emptyString()));

            var actual = assistant.select().dto().asOne();
            assertNull(actual);
        }

        @Test
        void givenListOfId_whenDeleteBatch_thenReturnNoContentStatus() {
            var givenToken = TestTokenUtils.requestToGetNewToken();
            var givenBody = assistant.populate(2)
                    .dto()
                    .asList()
                    .stream()
                    .map(RoleDto::getId)
                    .toList();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri("http://" + TEST_HOSTNAME).port(port).basePath(BASE_PATH + "/batch")
                    .body(givenBody)
                    .when().delete()
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value())
                    .body(is(emptyString()));

            var actual = assistant.select().dto().asList();
            assertTrue(actual.isEmpty());
        }

        @Test
        void givenNothing_whenDeleteAll_thenDeleteEveryThingWithNoContentStatus() {
            var givenToken = TestTokenUtils.requestToGetNewToken();
            assistant.populate(2);

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri("http://" + TEST_HOSTNAME).port(port).basePath(BASE_PATH)
                    .when().delete()
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value())
                    .body(is(emptyString()));

            var actual = assistant.select().dto().asList();
            assertTrue(actual.isEmpty());
        }
    }

    @Nested
    class ExistsTests {

        @Test
        void givenId_whenExists_ThenReturnOKStatus() {
            var givenToken = TestTokenUtils.requestToGetNewToken();
            var givenId = assistant.populate(1).dto().asOne().getId();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri("http://" + TEST_HOSTNAME).port(port).basePath(BASE_PATH + "/{id}")
                    .pathParam("id", givenId)
                    .when().head()
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body(is(emptyString()));
        }

        @Test
        void givenId_whenNotExist_ThenReturnNotFoundStatus() {
            var givenToken = TestTokenUtils.requestToGetNewToken();
            var givenId = -1;

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header("Authorization", "Bearer " + givenToken)
                    .baseUri("http://" + TEST_HOSTNAME).port(port).basePath(BASE_PATH + "/{id}")
                    .pathParam("id", givenId)
                    .when().head()
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body(is(emptyString()));
        }
    }
}
