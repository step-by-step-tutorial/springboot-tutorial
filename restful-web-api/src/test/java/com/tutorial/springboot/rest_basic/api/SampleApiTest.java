package com.tutorial.springboot.rest_basic.api;

import com.tutorial.springboot.rest_basic.TestApiUtils;
import com.tutorial.springboot.rest_basic.TestFixture;
import com.tutorial.springboot.rest_basic.dto.SampleDto;
import com.tutorial.springboot.rest_basic.service.SampleService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;
import java.util.List;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpMethod.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayName("Sample API Unit Tests")
class SampleApiTest {

    static final String ROOT_URI = "api/v1/samples";

    static final int OK_STATUS_CODE = 200;

    static final int CREATED_STATUS_CODE = 201;

    static final int NO_CONTENT_STATUS_CODE = 204;

    static final int BAD_REQUEST_STATUS_CODE = 400;

    static final int NOT_FOUND_STATUS_CODE = 404;

    static final int UNSUPPORTED_MEDIA_STATUS_CODE = 415;

    static final int INTERNAL_SERVER_ERROR_STATUS_CODE = 500;


    @Autowired
    TestRestTemplate systemUnderTest;

    @Autowired
    SampleService sampleService;

    UriComponentsBuilder uriBuilder;

    SampleApiTest(@LocalServerPort int port) {
        uriBuilder = TestApiUtils.uriBuilder(port, ROOT_URI);
    }

    @Nested
    @DisplayName("Persist data via REST API using POST method")
    class SaveTest {

        @Test
        void givenDto_whenPost_thenReturnIdOnCreatedStatus() {
            final var givenDto = TestFixture.oneSample();
            final var givenUri = uriBuilder.build().toUri();

            final var actual = systemUnderTest.exchange(givenUri, POST, new HttpEntity<>(givenDto), Long.class);

            assertNotNull(actual);
            assertEquals(CREATED_STATUS_CODE, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
            assertNotNull(actual.getHeaders().getLocation());
            assertEquals(givenUri + "/" + actual.getBody(), actual.getHeaders().getLocation().toString());
        }

        @Test
        void givenNullAsBody_whenPost_thenReturnUnsupportedMediaError() {
            final SampleDto givenDto = null;
            final var givenUri = uriBuilder.build().toUri();

            final var actual = systemUnderTest.exchange(givenUri, POST, new HttpEntity<>(givenDto), String.class);

            assertNotNull(actual);
            assertEquals(UNSUPPORTED_MEDIA_STATUS_CODE, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void givenDtoWithInvalidFields_whenPost_thenReturnBadRequestError() {
            final var givenDto = SampleDto.builder().build();
            final var givenUri = uriBuilder.build().toUri();

            final var actual = systemUnderTest.exchange(
                    givenUri,
                    POST,
                    new HttpEntity<>(givenDto),
                    new ParameterizedTypeReference<List<String>>() {
                    }
            );

            assertNotNull(actual);
            assertEquals(BAD_REQUEST_STATUS_CODE, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
            assertEquals(3, actual.getBody().size());
        }
    }

    @Nested
    @DisplayName("Persist batch of data via REST API using POST method")
    class SaveBatchTest {

        @Test
        void givenListOfDto_whenPost_thenReturnListOfIdOnCreatedStatus() {
            final var givenListOfDto = TestFixture.multiSample();
            final var givenUri = uriBuilder.path("/batch").build().toUri();

            final var actual = systemUnderTest.exchange(
                    givenUri,
                    POST,
                    new HttpEntity<>(givenListOfDto),
                    new ParameterizedTypeReference<List<Long>>() {
                    }
            );

            assertNotNull(actual);
            assertEquals(CREATED_STATUS_CODE, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
            assertEquals(givenListOfDto.length, actual.getBody().size());
        }

        @Test
        void givenListOfDtoIncludeInvalidDto_whenPost_thenReturnListOfIdForValidDtoOnCreatedStatus() {
            final var givenListOfDto = new SampleDto[]{SampleDto.builder().build(), TestFixture.oneSample()};

            final var givenUri = uriBuilder.path("/batch").build().toUri();

            final var actual = systemUnderTest.exchange(
                    givenUri,
                    POST,
                    new HttpEntity<>(givenListOfDto),
                    new ParameterizedTypeReference<List<Long>>() {
                    }
            );

            assertNotNull(actual);
            assertEquals(CREATED_STATUS_CODE, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
            assertEquals(1, actual.getBody().size());
        }

        @Test
        void givenListOfDtoIsIncludedOnlyInvalidDto_whenPost_thenReturnBadRequestError() {
            final var givenListOfDto = new SampleDto[]{SampleDto.builder().build(), SampleDto.builder().build()};

            final var givenUri = uriBuilder.path("/batch").build().toUri();

            final var actual = systemUnderTest.exchange(
                    givenUri,
                    POST,
                    new HttpEntity<>(givenListOfDto),
                    new ParameterizedTypeReference<List<String>>() {
                    }
            );

            assertNotNull(actual);
            assertEquals(BAD_REQUEST_STATUS_CODE, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void givenEmptyList_whenPost_thenReturnBadRequestError() {
            final var givenListOfDto = List.of();
            final var givenUri = uriBuilder.path("/batch").build().toUri();

            final var actual = systemUnderTest.exchange(givenUri, POST, new HttpEntity<>(givenListOfDto), String.class);

            assertNotNull(actual);
            assertEquals(BAD_REQUEST_STATUS_CODE, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void givenNullList_whenPost_thenReturnUnsupportedMediaError() {
            final SampleDto[] givenListOfDto = null;
            final var givenUri = uriBuilder.path("/batch").build().toUri();

            final var actual = systemUnderTest.exchange(givenUri, POST, new HttpEntity<>(givenListOfDto), String.class);

            assertNotNull(actual);
            assertEquals(UNSUPPORTED_MEDIA_STATUS_CODE, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }
    }

    @Nested
    @DisplayName("Fetch data via REST API using GET method")
    class FindByIdTest {

        private List<Long> listOfIdentities;

        @BeforeEach
        void populate() {
            listOfIdentities = sampleService.insertBatch(TestFixture.multiSample());
        }

        @AfterEach
        void truncate() {
            sampleService.deleteAll();
        }

        @Test
        void givenId_whenGet_thenReturnUniqueSampleDtoOnOkStatus() {
            final var givenId = TestFixture.selectByRandom(listOfIdentities, Long.class);
            final var givenUri = uriBuilder.path("/{id}").build(givenId);

            final var actual = systemUnderTest.exchange(givenUri, GET, new HttpEntity<>(null), SampleDto.class);

            assertNotNull(actual);
            assertEquals(OK_STATUS_CODE, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
            assertNotNull(actual.getBody().text());
            assertNotNull(actual.getBody().code());
            assertNotNull(actual.getBody().datetime());
        }

        @Test
        void givenNullId_whenGet_thenReturnNotFoundError() {
            final Long givenId = null;
            final var givenUri = uriBuilder.path("/{id}").build(givenId);

            final var actual = systemUnderTest.exchange(givenUri, GET, new HttpEntity<>(null), String.class);

            assertNotNull(actual);
            assertEquals(NOT_FOUND_STATUS_CODE, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void givenEmptyId_whenGet_thenReturnNotFoundError() {
            final var givenId = "";
            final var givenUri = uriBuilder.path("/{id}").build(givenId);

            final var actual = systemUnderTest.exchange(givenUri, GET, new HttpEntity<>(null), String.class);

            assertNotNull(actual);
            assertEquals(NOT_FOUND_STATUS_CODE, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void givenNotNumberId_whenGet_thenReturnInternalServerError() {
            final var givenId = "not number";
            final var givenUri = uriBuilder.path("/{id}").build(givenId);

            final var actual = systemUnderTest.exchange(givenUri, GET, new HttpEntity<>(null), String.class);

            assertNotNull(actual);
            assertEquals(INTERNAL_SERVER_ERROR_STATUS_CODE, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }
    }

    @Nested
    @DisplayName("Fetch a group of data via REST API using GET method")
    class FindAllTest {

        @BeforeEach
        void populate() {
            sampleService.insertBatch(TestFixture.multiSample());
        }

        @AfterEach
        void truncate() {
            sampleService.deleteAll();
        }

        @Test
        void givenNothing_whenGet_thenReturnListOfSampleDtoOnOkStatus() {
            final var givenUri = uriBuilder.build().toUri();

            final var actual = systemUnderTest.exchange(
                    givenUri,
                    GET,
                    new HttpEntity<>(null),
                    new ParameterizedTypeReference<List<SampleDto>>() {
                    });

            assertNotNull(actual);
            assertEquals(OK_STATUS_CODE, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
            assertFalse(actual.getBody().isEmpty());
        }
    }

    @Nested
    @DisplayName("Update data via REST API using PUT method")
    class UpdateTest {

        private List<Long> listOfIdentities;

        @BeforeEach
        void populate() {
            listOfIdentities = sampleService.insertBatch(TestFixture.multiSample());
        }

        @AfterEach
        void truncate() {
            sampleService.deleteAll();
        }

        @Test
        void givenDtoWithNullIdAndId_whenPut_thenReturnNothingOnNoContentStatus() {
            final var givenId = TestFixture.selectByRandom(listOfIdentities, Long.class);
            final var givenUri = uriBuilder.path("/{id}").build(givenId);
            final var givenDto = SampleDto.builder()
                    .text("update")
                    .code(1)
                    .datetime(now())
                    .version(1)
                    .build();

            final var actual = systemUnderTest.exchange(givenUri, PUT, new HttpEntity<>(givenDto), Void.class);

            assertNotNull(actual);
            assertEquals(NO_CONTENT_STATUS_CODE, actual.getStatusCode().value());
            assertNull(actual.getBody());
        }

        @Test
        void givenDtoWithTheSameIdAndId_whenPut_thenReturnNothingOnNoContentStatus() {
            final var givenId = TestFixture.selectByRandom(listOfIdentities, Long.class);
            final var givenUri = uriBuilder.path("/{id}").build(givenId);
            final var givenDto = SampleDto.builder()
                    .id(givenId)
                    .text("update")
                    .code(1)
                    .datetime(now())
                    .version(1)
                    .build();

            final var actual = systemUnderTest.exchange(givenUri, PUT, new HttpEntity<>(givenDto), Void.class);

            assertNotNull(actual);
            assertEquals(NO_CONTENT_STATUS_CODE, actual.getStatusCode().value());
            assertNull(actual.getBody());
        }

        @Test
        void givenNullAsBodyAndId_whenPut_thenReturnInternalServerError() {
            final var givenId = TestFixture.selectByRandom(listOfIdentities, Long.class);
            final var givenUri = uriBuilder.path("/{id}").build(givenId);
            final SampleDto givenDto = null;

            final var actual = systemUnderTest.exchange(givenUri, PUT, new HttpEntity<>(givenDto), String.class);

            assertNotNull(actual);
            assertEquals(INTERNAL_SERVER_ERROR_STATUS_CODE, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void givenEmptyDtoAndId_whenPut_thenReturnBadRequestError() {
            final var givenId = TestFixture.selectByRandom(listOfIdentities, Long.class);
            final var givenUri = uriBuilder.path("/{id}").build(givenId);
            final var givenDto = SampleDto.builder().build();

            final var actual = systemUnderTest.exchange(
                    givenUri,
                    PUT,
                    new HttpEntity<>(givenDto),
                    new ParameterizedTypeReference<List<String>>() {
                    });

            assertNotNull(actual);
            assertEquals(BAD_REQUEST_STATUS_CODE, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
            assertEquals(3, actual.getBody().size());
        }

        @Test
        void givenDtoWithDifferentIdAndId_whenPut_thenReturnBadRequestError() {
            final var givenId = TestFixture.selectByRandom(listOfIdentities, Long.class);
            final var givenUri = uriBuilder.path("/{id}").build(givenId);
            final var givenDto = SampleDto.builder()
                    .id(0L)
                    .text("updated")
                    .code(1)
                    .datetime(now())
                    .build();

            final var actual = systemUnderTest.exchange(
                    givenUri,
                    PUT,
                    new HttpEntity<>(givenDto),
                    new ParameterizedTypeReference<List<String>>() {
                    });

            assertNotNull(actual);
            assertEquals(BAD_REQUEST_STATUS_CODE, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
            assertEquals(1, actual.getBody().size());
        }

        @Test
        void givenDtoAndNullAsId_whenPut_thenReturnNotFoundError() {
            final Long givenId = null;
            final var givenUri = uriBuilder.path("/{id}").build(givenId);
            final var givenDto = SampleDto.builder().build();

            final var actual = systemUnderTest.exchange(givenUri, PUT, new HttpEntity<>(givenDto), String.class);

            assertNotNull(actual);
            assertEquals(NOT_FOUND_STATUS_CODE, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void givenDtoAndEmptyId_whenPut_thenReturnNotFoundError() {
            final var givenId = "";
            final var givenUri = uriBuilder.path("/{id}").build(givenId);
            final var givenDto = SampleDto.builder().build();

            final var actual = systemUnderTest.exchange(givenUri, PUT, new HttpEntity<>(givenDto), String.class);

            assertNotNull(actual);
            assertEquals(NOT_FOUND_STATUS_CODE, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void givenDtoAndNotNumberId_whenPut_thenReturnInternalServerError() {
            final var givenId = "not number";
            final var givenUri = uriBuilder.path("/{id}").build(givenId);
            final var givenDto = SampleDto.builder().build();

            final var actual = systemUnderTest.exchange(givenUri, PUT, new HttpEntity<>(givenDto), String.class);

            assertNotNull(actual);
            assertEquals(INTERNAL_SERVER_ERROR_STATUS_CODE, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }
    }

    @Nested
    @DisplayName("Delete data via REST API using DELETE method")
    class DeleteTest {

        private List<Long> listOfIdentities;

        @BeforeEach
        void populate() {
            listOfIdentities = sampleService.insertBatch(TestFixture.multiSample());
        }

        @AfterEach
        void truncate() {
            sampleService.deleteAll();
        }

        @Test
        void givenId_whenDelete_thenReturnNothingOnNoContentStatus() {
            final var givenId = TestFixture.selectByRandom(listOfIdentities, Long.class);
            final var givenUri = uriBuilder.path("/{id}").build(givenId);

            final var actual = systemUnderTest.exchange(givenUri, DELETE, new HttpEntity<>(null), Void.class);

            assertNotNull(actual);
            assertEquals(NO_CONTENT_STATUS_CODE, actual.getStatusCode().value());
            assertNull(actual.getBody());
        }

        @Test
        void givenNullAsId_whenDelete_thenReturnNotFoundError() {
            final Long givenId = null;
            final var givenUri = uriBuilder.path("/{id}").build(givenId);

            final var actual = systemUnderTest.exchange(givenUri, DELETE, new HttpEntity<>(null), String.class);

            assertNotNull(actual);
            assertEquals(NOT_FOUND_STATUS_CODE, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void givenEmptyId_whenDelete_thenReturnNotFoundError() {
            final var givenId = "";
            final var givenUri = uriBuilder.path("/{id}").build(givenId);

            final var actual = systemUnderTest.exchange(givenUri, DELETE, new HttpEntity<>(null), String.class);

            assertNotNull(actual);
            assertEquals(NOT_FOUND_STATUS_CODE, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void givenNotNumberId_whenDelete_thenReturnInternalServerError() {
            final var givenId = "not number";
            final var givenUri = uriBuilder.path("/{id}").build(givenId);

            final var actual = systemUnderTest.exchange(givenUri, DELETE, new HttpEntity<>(null), String.class);

            assertNotNull(actual);
            assertEquals(INTERNAL_SERVER_ERROR_STATUS_CODE, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }
    }

    @Nested
    @DisplayName("Delete a group of data via REST API using DELETE method")
    class DeleteBatchTest {

        @BeforeEach
        void populate() {
            sampleService.insertBatch(TestFixture.multiSample());
        }

        @AfterEach
        void truncate() {
            sampleService.deleteAll();
        }

        @Test
        void givenNothing_whenDeleteAll_thenReturnNothingOnNoContentStatus() {
            final var givenUri = uriBuilder.build().toUri();

            final var actual = systemUnderTest.exchange(givenUri, DELETE, new HttpEntity<>(null), Void.class);

            assertNotNull(actual);
            assertEquals(NO_CONTENT_STATUS_CODE, actual.getStatusCode().value());
            assertNull(actual.getBody());
        }

        @Test
        void givenListOfId_whenDeleteBatch_thenReturnNothingOnNoContentStatus() {
            var givenIdentities = sampleService.selectIdentities();
            final var givenUri = uriBuilder.path("/batch").build().toUri();

            final var actual = systemUnderTest.exchange(givenUri, DELETE, new HttpEntity<>(givenIdentities), Void.class);

            assertNotNull(actual);
            assertEquals(NO_CONTENT_STATUS_CODE, actual.getStatusCode().value());
            assertNull(actual.getBody());
        }

        @Test
        void givenNullAsListOfId_whenDeleteBatch_thenReturnInternalServerError() {
            final Long[] givenIdentities = null;
            final var givenUri = uriBuilder.path("/batch").build().toUri();

            final var actual = systemUnderTest.exchange(givenUri, DELETE, new HttpEntity<>(givenIdentities), String.class);

            assertNotNull(actual);
            assertEquals(INTERNAL_SERVER_ERROR_STATUS_CODE, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void givenEmptyList_whenDeleteBatch_thenReturnBadRequestError() {
            final var givenIdentities = List.of();
            final var givenUri = uriBuilder.path("/batch").build().toUri();

            final var actual = systemUnderTest.exchange(givenUri, DELETE, new HttpEntity<>(givenIdentities), String.class);

            assertNotNull(actual);
            assertEquals(BAD_REQUEST_STATUS_CODE, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void givenListOfIdWithNotNumberElement_whenDeleteBatch_thenReturnInternalServerError() {
            final var givenIdentities = new Object[]{"not number"};
            final var givenUri = uriBuilder.path("/batch").build().toUri();

            final var actual = systemUnderTest.exchange(givenUri, DELETE, new HttpEntity<>(givenIdentities), String.class);

            assertNotNull(actual);
            assertEquals(INTERNAL_SERVER_ERROR_STATUS_CODE, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void givenListOfIdWithNullElement_whenDelete_thenReturnNothingOnNoContentStatus() {
            final var givenIdentities = new Long[]{1L, null};
            final var givenUri = uriBuilder.path("/batch").build().toUri();

            final var actual = systemUnderTest.exchange(givenUri, DELETE, new HttpEntity<>(givenIdentities), String.class);

            assertNotNull(actual);
            assertEquals(NO_CONTENT_STATUS_CODE, actual.getStatusCode().value());
            assertNull(actual.getBody());
        }

        @Test
        void givenListOfIdWithEmptyElement_whenDelete_thenReturnNothingOnNoContentStatus() {
            final var givenIdentities = new Object[]{1, ""};
            final var givenUri = uriBuilder.path("/batch").build().toUri();

            final var actual = systemUnderTest.exchange(givenUri, DELETE, new HttpEntity<>(givenIdentities), String.class);

            assertNotNull(actual);
            assertEquals(NO_CONTENT_STATUS_CODE, actual.getStatusCode().value());
            assertNull(actual.getBody());
        }
    }

    @Nested
    @DisplayName("Fetch metadata via REST API using HEAD,OPTION method")
    class GetMetadata {

        private List<Long> listOfIdentities;

        @BeforeEach
        void populate() {
            listOfIdentities = sampleService.insertBatch(TestFixture.multiSample());
        }

        @AfterEach
        void truncate() {
            sampleService.deleteAll();
        }

        @Test
        void givenId_whenHead_thenReturnOkStatus() {
            final var givenId = TestFixture.selectByRandom(listOfIdentities, Long.class);
            final var givenUri = uriBuilder.path("/{id}").build(givenId);

            final var actual = systemUnderTest.exchange(givenUri, HEAD, new HttpEntity<>(null), SampleDto.class);

            assertNotNull(actual);
            assertEquals(OK_STATUS_CODE, actual.getStatusCode().value());
        }

        @Test
        void givenInvalidId_whenHead_thenReturnNotFoundStatus() {
            final var givenId = -1;
            final var givenUri = uriBuilder.path("/{id}").build(givenId);

            final var actual = systemUnderTest.exchange(givenUri, HEAD, new HttpEntity<>(null), SampleDto.class);

            assertNotNull(actual);
            assertEquals(NOT_FOUND_STATUS_CODE, actual.getStatusCode().value());
        }

        @Test
        void givenNothing_whenOption_thenReturnAllowedHttpVerbsOnOkStatus() {
            final var givenUri = uriBuilder.path("/options").build().toUri();

            final var actual = systemUnderTest.exchange(givenUri, OPTIONS, new HttpEntity<>(null), String.class);

            assertNotNull(actual);
            assertEquals(OK_STATUS_CODE, actual.getStatusCode().value());
        }
    }
}
