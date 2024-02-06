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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpMethod.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayName("Sample API Unit Tests")
class SampleApiTest {

    static final String ROOT_URI = "api/v1/samples";

    @Autowired
    TestRestTemplate systemUnderTest;

    @Autowired
    SampleService sampleService;

    UriComponentsBuilder uriBuilder;

    public SampleApiTest(@LocalServerPort int port) {
        uriBuilder = TestApiUtils.uriBuilder(port, ROOT_URI);
    }

    @Nested
    @DisplayName("POST -> save one")
    class SaveTest {

        @Test
        void givenDto_whenPost_thenReturnIdOnCreatedStatus() {
            final var givenDto = TestFixture.oneSample();
            final var givenUri = uriBuilder.build().toUri();

            final var actual = systemUnderTest.exchange(givenUri, POST, new HttpEntity<>(givenDto), Long.class);

            assertNotNull(actual);
            assertEquals(201, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
            assertNotNull(actual.getHeaders().getLocation());
            assertEquals(givenUri + "/" + actual.getBody(), actual.getHeaders().getLocation().toString());
        }

        @Test
        void givenNullDto_whenPost_thenReturnUnsupportedMediaError() {
            final SampleDto givenDto = null;
            final var givenUri = uriBuilder.build().toUri();

            final var actual = systemUnderTest.exchange(givenUri, POST, new HttpEntity<>(givenDto), String.class);

            assertNotNull(actual);
            assertEquals(415, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void givenDtoWithInvalidField_whenPost_thenReturnInternalServerError() {
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
            assertEquals(400, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
            assertFalse(actual.getBody().isEmpty());
        }
    }

    @Nested
    @DisplayName("POST -> save all")
    class SaveAllTest {

        @Test
        void givenListOfDto_whenPost_thenReturnListOfIdOnCreatedStatus() {
            final var givenListOfDto = TestFixture.multiSample();
            final var givenUri = uriBuilder.path("/saveAll").build().toUri();

            final var actual = systemUnderTest.exchange(
                    givenUri,
                    POST,
                    new HttpEntity<>(givenListOfDto),
                    new ParameterizedTypeReference<List<Long>>() {
                    }
            );

            assertNotNull(actual);
            assertEquals(201, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
            assertEquals(givenListOfDto.length, actual.getBody().size());
        }

        @Test
        void givenEmptyList_whenPost_thenReturnInternalServerError() {
            final var givenListOfDto = new SampleDto[0];
            final var givenUri = uriBuilder.path("/saveAll").build().toUri();

            final var actual = systemUnderTest.exchange(givenUri, POST, new HttpEntity<>(givenListOfDto), String.class);

            assertNotNull(actual);
            assertEquals(500, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void givenNullList_whenPost_thenReturnUnsupportedMediaError() {
            final SampleDto[] givenListOfDto = null;
            final var givenUri = uriBuilder.path("/saveAll").build().toUri();

            final var actual = systemUnderTest.exchange(givenUri, POST, new HttpEntity<>(givenListOfDto), String.class);

            assertNotNull(actual);
            assertEquals(415, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }
    }

    @Nested
    @DisplayName("GET -> find one")
    class FindByIdTest {

        private List<Long> listOfIdentities;

        @BeforeEach
        void populate() {
            listOfIdentities = sampleService.insertAll(TestFixture.multiSample());
        }

        @AfterEach
        void truncate() {
            sampleService.truncate();
        }

        @Test
        void givenId_whenGet_thenReturnUniqueSampleDtoOnOkStatus() {
            final var givenId = TestFixture.selectByRandom(listOfIdentities, Long.class);
            final var givenUri = uriBuilder.path("/{id}").build(givenId);

            final var actual = systemUnderTest.exchange(givenUri, GET, new HttpEntity<>(null), SampleDto.class);

            assertNotNull(actual);
            assertEquals(200, actual.getStatusCode().value());
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
            assertEquals(404, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void givenEmptyId_whenGet_thenReturnNotFoundError() {
            final String givenId = "";
            final var givenUri = uriBuilder.path("/{id}").build(givenId);

            final var actual = systemUnderTest.exchange(givenUri, GET, new HttpEntity<>(null), String.class);

            assertNotNull(actual);
            assertEquals(404, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void givenNotNumberId_whenGet_thenReturnInternalServerError() {
            final String givenId = "not number";
            final var givenUri = uriBuilder.path("/{id}").build(givenId);

            final var actual = systemUnderTest.exchange(givenUri, GET, new HttpEntity<>(null), String.class);

            assertNotNull(actual);
            assertEquals(500, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }
    }

    @Nested
    @DisplayName("GET -> find all")
    class FindAllTest {

        @BeforeEach
        void populate() {
            sampleService.insertAll(TestFixture.multiSample());
        }

        @AfterEach
        void truncate() {
            sampleService.truncate();
        }

        @Test
        void givenNothing_whenGet_thenReturnListOfSampleDtoOnOkStatus() {
            final var givenUri = uriBuilder.path("/findAll").build().toUri();

            final var actual = systemUnderTest.exchange(
                    givenUri,
                    GET,
                    new HttpEntity<>(null),
                    new ParameterizedTypeReference<List<SampleDto>>() {
                    });

            assertNotNull(actual);
            assertEquals(200, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
            assertFalse(actual.getBody().isEmpty());
        }

        @Test
        void givenIdSequence_whenGet_thenReturnListOfSampleDtoOnOkStatus() {
            var givenIdentities = sampleService.getIdentities()
                    .stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            final var givenUri = uriBuilder.path("/findAll/{identities}").build(givenIdentities);

            final var actual = systemUnderTest.exchange(givenUri, GET, new HttpEntity<>(null), String.class);

            assertNotNull(actual);
            assertEquals(200, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
            assertFalse(actual.getBody().isEmpty());
        }

        @Test
        void givenNullAsIdSequence_whenGet_thenReturnNotFoundError() {
            final String givenIdentities = null;
            final var givenUri = uriBuilder.path("/findAll/{identities}").build(givenIdentities);

            final var actual = systemUnderTest.exchange(givenUri, GET, new HttpEntity<>(null), String.class);

            assertNotNull(actual);
            assertEquals(404, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void givenEmptyStringAsIdSequence_whenGet_thenReturnNotFoundError() {
            final var givenIdentities = "";
            final var givenUri = uriBuilder.path("/findAll/{identities}").build(givenIdentities);

            final var actual = systemUnderTest.exchange(givenUri, GET, new HttpEntity<>(null), String.class);

            assertNotNull(actual);
            assertEquals(404, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void givenIdSequenceWithNotNumber_whenGet_thenReturnInternalServerError() {
            final var givenIdentities = "not number";
            final var givenUri = uriBuilder.path("/findAll/{identities}").build(givenIdentities);

            final var actual = systemUnderTest.exchange(givenUri, GET, new HttpEntity<>(null), String.class);

            assertNotNull(actual);
            assertEquals(500, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void givenIdSequenceWithNullPlace_whenGet_thenReturnInternalServerError() {
            final var givenIdentities = "1,,2";
            final var givenUri = uriBuilder.path("/findAll/{identities}").build(givenIdentities);

            final var actual = systemUnderTest.exchange(givenUri, GET, new HttpEntity<>(null), String.class);

            assertNotNull(actual);
            assertEquals(500, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }
    }

    @Nested
    @DisplayName("PUT -> update one")
    class UpdateTest {

        private List<Long> listOfIdentities;

        @BeforeEach
        void populate() {
            listOfIdentities = sampleService.insertAll(TestFixture.multiSample());
        }

        @AfterEach
        void truncate() {
            sampleService.truncate();
        }

        @Test
        void givenDtoAndId_whenPut_thenReturnNothingOnNoContentStatus() {
            final var givenId = TestFixture.selectByRandom(listOfIdentities, Long.class);
            final var givenUri = uriBuilder.path("/{id}").build(givenId);
            final var givenDto = SampleDto.builder()
                    .from(sampleService.selectById(givenId).orElseThrow())
                    .text("update")
                    .code(1)
                    .datetime(now())
                    .build();

            final var actual = systemUnderTest.exchange(givenUri, PUT, new HttpEntity<>(givenDto), Void.class);

            assertNotNull(actual);
            assertEquals(204, actual.getStatusCode().value());
            assertNull(actual.getBody());
        }

        @Test
        void givenNullDtoAndId_whenPut_thenReturnInternalServerError() {
            final var givenId = TestFixture.selectByRandom(listOfIdentities, Long.class);
            final var givenUri = uriBuilder.path("/{id}").build(givenId);
            final SampleDto givenDto = null;

            final var actual = systemUnderTest.exchange(givenUri, PUT, new HttpEntity<>(givenDto), String.class);

            assertNotNull(actual);
            assertEquals(500, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void givenEmptyDtoAndId_whenPut_thenReturnInternalServerError() {
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
            assertEquals(400, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void givenDtoAndNullId_whenPut_thenReturnNotFoundError() {
            final Long givenId = null;
            final var givenUri = uriBuilder.path("/{id}").build(givenId);
            final var givenDto = SampleDto.builder().build();

            final var actual = systemUnderTest.exchange(givenUri, PUT, new HttpEntity<>(givenDto), String.class);

            assertNotNull(actual);
            assertEquals(404, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void givenDtoAndEmptyId_whenPut_thenReturnNotFoundError() {
            final var givenId = "";
            final var givenUri = uriBuilder.path("/{id}").build(givenId);
            final var givenDto = SampleDto.builder().build();

            final var actual = systemUnderTest.exchange(givenUri, PUT, new HttpEntity<>(givenDto), String.class);

            assertNotNull(actual);
            assertEquals(404, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void givenDtoAndNotNumberId_whenPut_thenReturnInternalServerError() {
            final var givenId = "not number";
            final var givenUri = uriBuilder.path("/{id}").build(givenId);
            final var givenDto = SampleDto.builder().build();

            final var actual = systemUnderTest.exchange(givenUri, PUT, new HttpEntity<>(givenDto), String.class);

            assertNotNull(actual);
            assertEquals(500, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }
    }

    @Nested
    @DisplayName("DELETE -> delete one")
    class DeleteTest {

        private List<Long> listOfIdentities;

        @BeforeEach
        void populate() {
            listOfIdentities = sampleService.insertAll(TestFixture.multiSample());
        }

        @AfterEach
        void truncate() {
            sampleService.truncate();
        }

        @Test
        void givenId_whenDelete_thenReturnNothingOnNoContentStatus() {
            final var givenId = TestFixture.selectByRandom(listOfIdentities, Long.class);
            final var givenUri = uriBuilder.path("/{id}").build(givenId);

            final var actual = systemUnderTest.exchange(givenUri, DELETE, new HttpEntity<>(null), Void.class);

            assertNotNull(actual);
            assertEquals(204, actual.getStatusCode().value());
            assertNull(actual.getBody());
        }

        @Test
        void givenNullId_whenDelete_thenReturnInternalServerError() {
            final Long givenId = null;
            final var givenUri = uriBuilder.path("/{id}").build(givenId);

            final var actual = systemUnderTest.exchange(givenUri, DELETE, new HttpEntity<>(null), String.class);

            assertNotNull(actual);
            assertEquals(404, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void givenEmptyId_whenDelete_thenReturnInternalServerError() {
            final var givenId = "";
            final var givenUri = uriBuilder.path("/{id}").build(givenId);

            final var actual = systemUnderTest.exchange(givenUri, DELETE, new HttpEntity<>(null), String.class);

            assertNotNull(actual);
            assertEquals(404, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void givenNotNumberId_whenDelete_thenReturnInternalServerError() {
            final var givenId = "not number";
            final var givenUri = uriBuilder.path("/{id}").build(givenId);

            final var actual = systemUnderTest.exchange(givenUri, DELETE, new HttpEntity<>(null), String.class);

            assertNotNull(actual);
            assertEquals(500, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }
    }

    @Nested
    @DisplayName("DELETE -> delete all")
    class DeleteAllTest {

        @BeforeEach
        void populate() {
            sampleService.insertAll(TestFixture.multiSample());
        }

        @AfterEach
        void truncate() {
            sampleService.truncate();
        }

        @Test
        void givenNothing_whenDelete_thenReturnNothingOnNoContentStatus() {
            final var givenUri = uriBuilder.path("/truncate").build().toUri();

            final var actual = systemUnderTest.exchange(givenUri, DELETE, new HttpEntity<>(null), Void.class);

            assertNotNull(actual);
            assertEquals(204, actual.getStatusCode().value());
        }

        @Test
        void givenIdSequence_whenDelete_thenReturnNothingOnNoContentStatus() {
            var givenIdentities = sampleService.getIdentities()
                    .stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            final var givenUri = uriBuilder.path("/deleteAll/{identities}").build(givenIdentities);

            final var actual = systemUnderTest.exchange(givenUri, DELETE, new HttpEntity<>(null), Void.class);

            assertNotNull(actual);
            assertEquals(204, actual.getStatusCode().value());
        }

        @Test
        void givenNullAsIdSequence_whenDelete_thenReturnNotFoundError() {
            final String givenIdentities = null;
            final var givenUri = uriBuilder.path("/deleteAll/{identities}").build(givenIdentities);

            final var actual = systemUnderTest.exchange(givenUri, DELETE, new HttpEntity<>(null), String.class);

            assertNotNull(actual);
            assertEquals(404, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void givenEmptyStringAsIdSequence_whenDelete_thenReturnNotFoundError() {
            final var givenIdentities = "";
            final var givenUri = uriBuilder.path("/deleteAll/{identities}").build(givenIdentities);

            final var actual = systemUnderTest.exchange(givenUri, DELETE, new HttpEntity<>(null), String.class);

            assertNotNull(actual);
            assertEquals(404, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void givenIdSequenceWithNotNumber_whenDelete_thenReturnInternalServerError() {
            final var givenIdentities = "not number";
            final var givenUri = uriBuilder.path("/deleteAll/{identities}").build(givenIdentities);

            final var actual = systemUnderTest.exchange(givenUri, DELETE, new HttpEntity<>(null), String.class);

            assertNotNull(actual);
            assertEquals(500, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void givenIdSequenceWithNullPlace_whenDelete_thenReturnNotFoundError() {
            final var givenIdentities = "1,,2";
            final var givenUri = uriBuilder.path("/deleteAll/{identities}").build(givenIdentities);

            final var actual = systemUnderTest.exchange(givenUri, DELETE, new HttpEntity<>(null), String.class);

            assertNotNull(actual);
            assertEquals(500, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }
    }

    @Nested
    @DisplayName("HEAD,OPTION -> metadata")
    class GetMetadata {

        private List<Long> listOfIdentities;

        @BeforeEach
        void populate() {
            listOfIdentities = sampleService.insertAll(TestFixture.multiSample());
        }

        @AfterEach
        void truncate() {
            sampleService.truncate();
        }

        @Test
        void givenId_whenHead_thenReturnOkStatus() {
            final var givenId = TestFixture.selectByRandom(listOfIdentities, Long.class);
            final var givenUri = uriBuilder.path("/{id}").build(givenId);

            final var actual = systemUnderTest.exchange(givenUri, HEAD, new HttpEntity<>(null), SampleDto.class);

            assertNotNull(actual);
            assertEquals(200, actual.getStatusCode().value());
        }

        @Test
        void givenInvalidId_whenHead_thenReturnNotFoundStatus() {
            final var givenId = Collections.max(listOfIdentities) + 1;
            final var givenUri = uriBuilder.path("/{id}").build(givenId);

            final var actual = systemUnderTest.exchange(givenUri, HEAD, new HttpEntity<>(null), SampleDto.class);

            assertNotNull(actual);
            assertEquals(404, actual.getStatusCode().value());
        }

        @Test
        void givenNothing_whenOption_thenReturnAllowedHttpVerbsOnOkStatus() {
            final var givenUri = uriBuilder.path("/verbs").build().toUri();

            final var actual = systemUnderTest.exchange(givenUri, OPTIONS, new HttpEntity<>(null), String.class);

            assertNotNull(actual);
            assertEquals(200, actual.getStatusCode().value());
        }
    }
}
