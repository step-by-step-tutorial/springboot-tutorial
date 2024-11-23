package com.tutorial.springboot.restful_web_api.api;

import com.tutorial.springboot.restful_web_api.dto.SampleDto;
import com.tutorial.springboot.restful_web_api.service.SampleService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.List;

import static com.tutorial.springboot.restful_web_api.TestFixture.*;
import static io.restassured.RestAssured.given;
import static java.time.LocalDateTime.now;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SampleApiTest {

    static final String HOST = "http://localhost";

    @LocalServerPort
    int port;

    static final String ROOT_URI = "/api/v1/samples";

    @Autowired
    SampleService<Long, SampleDto> testAssistant;

    @Nested
    class SaveTest {

        @Test
        void givenDto_whenPost_thenReturnIdOnCreatedStatus() {
            var givenDto = oneSample();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .baseUri(HOST).port(port).basePath(ROOT_URI)
                    .body(givenDto)
                    .when().post()
                    .then()
                    .statusCode(CREATED.value())
                    .statusCode(HttpStatus.CREATED.value())
                    .header("Location", containsString(ROOT_URI))
                    .body("", notNullValue());
        }

        @Test
        void givenNullAsBody_whenPost_thenReturnInternalServerError() {
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .baseUri(HOST).port(port).basePath(ROOT_URI)
                    .when().post()
                    .then()
                    .statusCode(INTERNAL_SERVER_ERROR.value());
        }

        @Test
        void givenDtoWithInvalidFields_whenPost_thenReturnBadRequestError() {
            var givenDto = SampleDto.builder().build();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .baseUri(HOST).port(port).basePath(ROOT_URI)
                    .body(givenDto)
                    .when().post()
                    .then()
                    .statusCode(BAD_REQUEST.value())
                    .body("errors", hasSize(3));
        }
    }

    @Nested
    class SaveBatchTest {

        @Test
        void givenListOfDto_whenPost_thenReturnListOfIdOnCreatedStatus() {
            var givenListOfDto = multiSample();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .baseUri(HOST).port(port).basePath(ROOT_URI + "/batch")
                    .body(givenListOfDto)
                    .when().post()
                    .then()
                    .statusCode(CREATED.value())
                    .body("", hasSize(givenListOfDto.length));
        }

        @Test
        void givenListOfDtoIncludeInvalidDto_whenPost_thenReturnListOfIdForValidDtoOnCreatedStatus() {
            var givenListOfDto = new SampleDto[]{SampleDto.builder().build(), oneSample()};

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .baseUri(HOST).port(port).basePath(ROOT_URI + "/batch")
                    .body(givenListOfDto)
                    .when()
                    .post().then()
                    .statusCode(CREATED.value())
                    .body("", hasSize(1));
        }

        @Test
        void givenEmptyList_whenPost_thenReturnBadRequestError() {
            var givenListOfDto = List.of();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .baseUri(HOST).port(port).basePath(ROOT_URI + "/batch")
                    .body(givenListOfDto)
                    .when().post()
                    .then()
                    .statusCode(BAD_REQUEST.value());
        }

        @Test
        void givenNullList_whenPost_thenReturnInternalServerError() {
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .baseUri(HOST).port(port).basePath(ROOT_URI + "/batch")
                    .when().post()
                    .then()
                    .statusCode(INTERNAL_SERVER_ERROR.value());
        }
    }

    @Nested
    class FindByIdTest {

        private List<Long> identifiers;

        @BeforeEach
        void populate() {
            identifiers = testAssistant.saveBatch(multiSample());
        }

        @AfterEach
        void truncate() {
            testAssistant.deleteAll();
        }

        @Test
        void givenId_whenGet_thenReturnUniqueSampleDtoOnOkStatus() {
            var givenId = selectByRandom(identifiers, Long.class);

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .baseUri(HOST).port(port)
                    .basePath(ROOT_URI + "/{id}").pathParam("id", givenId)
                    .when().get()
                    .then()
                    .statusCode(OK.value())
                    .body("text", notNullValue())
                    .body("code", notNullValue())
                    .body("datetime", notNullValue());
        }

        @Test
        void givenEmptyId_whenGet_thenReturnNotFoundError() {
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .baseUri(HOST).port(port)
                    .basePath(ROOT_URI + "/{id}").pathParam("id", "")
                    .when().get()
                    .then()
                    .statusCode(NOT_FOUND.value());
        }

        @Test
        void givenNotNumberId_whenGet_thenReturnInternalServerError() {
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .baseUri(HOST).port(port)
                    .basePath(ROOT_URI + "/{id}").pathParam("id", "not number")
                    .when().get()
                    .then()
                    .statusCode(INTERNAL_SERVER_ERROR.value());
        }
    }

    @Nested
    class FindBatchTest {

        @BeforeEach
        void populate() {
            testAssistant.saveBatch(multiSample());
        }

        @AfterEach
        void truncate() {
            testAssistant.deleteAll();
        }

        @Test
        void givenValidPageAndSize_whenGet_thenReturnPageOfSamples() {
            var givenPage = 0;
            var givenSize = 2;

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .baseUri(HOST).port(port)
                    .basePath(ROOT_URI + "/batch/{page}/{size}")
                    .pathParam("page", givenPage)
                    .pathParam("size", givenSize)
                    .when().get()
                    .then()
                    .statusCode(OK.value())
                    .body("content", is(not(empty())))
                    .body("totalItems", greaterThanOrEqualTo(1));
        }

        @Test
        void givenInvalidPageAndSize_whenGet_thenReturnBadRequest() {
            var givenPage = -1;
            var givenSize = -1;

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .baseUri(HOST).port(port)
                    .basePath(ROOT_URI + "/batch/{page}/{size}")
                    .pathParam("page", givenPage)
                    .pathParam("size", givenSize)
                    .when().get()
                    .then()
                    .statusCode(INTERNAL_SERVER_ERROR.value());
        }

        @Test
        void givenExceedingPage_whenGet_thenReturnEmptyPage() {
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .baseUri(HOST).port(port)
                    .basePath(ROOT_URI + "/batch/{page}/{size}")
                    .pathParam("page", 100)
                    .pathParam("size", 2)
                    .when().get()
                    .then()
                    .statusCode(OK.value())
                    .body("content", hasSize(0));
        }
    }

    @Nested
    class FindAllTest {

        @BeforeEach
        void populate() {
            testAssistant.saveBatch(multiSample());
        }

        @AfterEach
        void truncate() {
            testAssistant.deleteAll();
        }

        @Test
        void givenRequest_whenGet_thenReturnAllSamples() {
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .baseUri(HOST).port(port).basePath(ROOT_URI)
                    .when().get()
                    .then()
                    .statusCode(OK.value())
                    .body("size()", greaterThan(0));
        }
    }

    @Nested
    class UpdateTest {

        private List<Long> identifiers;

        @BeforeEach
        void populate() {
            identifiers = testAssistant.saveBatch(multiSample());
        }

        @AfterEach
        void truncate() {
            testAssistant.deleteAll();
        }

        @Test
        void givenDtoWithNullIdAndId_whenPut_thenReturnNothingOnNoContentStatus() {
            var givenId = selectByRandom(identifiers, Long.class);
            var givenDto = SampleDto.builder()
                    .text("update")
                    .code(1)
                    .datetime(now())
                    .version(1)
                    .build();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .baseUri(HOST).port(port).basePath(ROOT_URI + "/{id}")
                    .pathParam("id", givenId)
                    .body(givenDto)
                    .when().put()
                    .then()
                    .statusCode(NO_CONTENT.value());
        }

        @Test
        void givenEmptyDtoAndId_whenPut_thenReturnBadRequestError() {
            var givenId = selectByRandom(identifiers, Long.class);
            var givenDto = SampleDto.builder().build();

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .baseUri(HOST).port(port)
                    .basePath(ROOT_URI + "/{id}").pathParam("id", givenId)
                    .body(givenDto)
                    .when().put()
                    .then()
                    .statusCode(BAD_REQUEST.value())
                    .body("errors", hasSize(3));
        }
    }

    @Nested
    class DeleteTest {

        private List<Long> identifiers;

        @BeforeEach
        void populate() {
            identifiers = testAssistant.saveBatch(multiSample());
        }

        @AfterEach
        void truncate() {
            testAssistant.deleteAll();
        }

        @Test
        void givenId_whenDelete_thenReturnNothingOnNoContentStatus() {
            var givenId = selectByRandom(identifiers, Long.class);

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .baseUri(HOST).port(port)
                    .basePath(ROOT_URI + "/{id}").pathParam("id", givenId)
                    .when().delete()
                    .then()
                    .statusCode(NO_CONTENT.value());
        }

        @Test
        void givenNotNumberId_whenDelete_thenReturnInternalServerError() {
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .baseUri(HOST).port(port)
                    .basePath(ROOT_URI + "/{id}").pathParam("id", "not number")
                    .when().delete()
                    .then()
                    .statusCode(INTERNAL_SERVER_ERROR.value());
        }
    }

    @Nested
    class DeleteBatchTest {

        private List<Long> identifiers;

        @BeforeEach
        void populate() {
            identifiers = testAssistant.saveBatch(multiSample());
        }

        @AfterEach
        void truncate() {
            testAssistant.deleteAll();
        }

        @Test
        void givenValidIdentifiers_whenDeleteBatch_thenReturnNoContentStatus() {
            var givenBody = identifiers.toArray(Long[]::new);

            given()
                    .contentType(ContentType.JSON)
                    .baseUri(HOST).port(port)
                    .basePath(ROOT_URI + "/batch")
                    .body(givenBody)
                    .when().delete()
                    .then()
                    .statusCode(NO_CONTENT.value());

            var actual = testAssistant.findAll();

            assertNotNull(actual);
            assertTrue(actual.isEmpty());
        }

        @Test
        void givenEmptyIdentifiers_whenDeleteBatch_thenReturnBadRequestStatus() {
            var givenBody = new Long[]{};

            given()
                    .contentType(ContentType.JSON)
                    .baseUri(HOST).port(port)
                    .basePath(ROOT_URI + "/batch")
                    .body(givenBody)
                    .when().delete()
                    .then()
                    .statusCode(BAD_REQUEST.value());
        }

        @Test
        void givenNullIdentifiers_whenDeleteBatch_thenReturnInternalServerError() {
            var givenBody = "";

            given()
                    .contentType(ContentType.JSON)
                    .baseUri(HOST).port(port)
                    .basePath(ROOT_URI + "/batch")
                    .body(givenBody)
                    .when().delete()
                    .then()
                    .statusCode(INTERNAL_SERVER_ERROR.value());
        }
    }

    @Nested
    class DeleteAllTest {

        @BeforeEach
        void populate() {
            testAssistant.saveBatch(multiSample());
        }

        @AfterEach
        void truncate() {
            testAssistant.deleteAll();
        }

        @Test
        void givenRequestToDeleteAll_whenDelete_thenReturnNoContentStatus() {
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .baseUri(HOST).port(port).basePath(ROOT_URI)
                    .when().delete()
                    .then()
                    .statusCode(NO_CONTENT.value());

            var actual = testAssistant.findAll();

            assertNotNull(actual);
            assertTrue(actual.isEmpty());
        }
    }

    @Nested
    class GetMetadata {

        private List<Long> identifiers;

        @BeforeEach
        void populate() {
            identifiers = testAssistant.saveBatch(multiSample());
        }

        @AfterEach
        void truncate() {
            testAssistant.deleteAll();
        }

        @Test
        void givenId_whenHead_thenReturnOkStatus() {
            var givenId = selectByRandom(identifiers, Long.class);

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .baseUri(HOST).port(port)
                    .basePath(ROOT_URI + "/{id}").pathParam("id", givenId)
                    .when().head()
                    .then()
                    .statusCode(OK.value());
        }

        @Test
        void givenNothing_whenOption_thenReturnAllowedHttpVerbsOnOkStatus() {
            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .baseUri(HOST).port(port).basePath(ROOT_URI + "/options")
                    .when().options()
                    .then()
                    .statusCode(OK.value())
                    .header("Allow", notNullValue());
        }

    }

}