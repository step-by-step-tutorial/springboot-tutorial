package com.tutorial.springboot.rest_basic.api;

import com.tutorial.springboot.rest_basic.TestApiUtils;
import com.tutorial.springboot.rest_basic.TestFixtureUtils;
import com.tutorial.springboot.rest_basic.dao.SampleRepository;
import com.tutorial.springboot.rest_basic.dto.SampleDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpMethod.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayName("Sample API unit tests")
class SampleApiTest {

    static final String ROOT_URI = "api/v1/samples";

    @Autowired
    TestRestTemplate systemUnderTest;

    @Autowired
    SampleRepository sampleRepository;

    UriComponentsBuilder uriBuilder;

    public SampleApiTest(@LocalServerPort int port) {
        uriBuilder = TestApiUtils.uriBuilder(port, ROOT_URI);
    }

    static class TestFixture {
        static SampleDto oneSample() {
            return SampleDto.builder()
                    .text("fake")
                    .code(1)
                    .datetime(LocalDateTime.now())
                    .build();
        }

        static SampleDto[] multiSample(final int number) {
            return IntStream.range(0, number)
                    .boxed()
                    .map(integer -> SampleDto.builder()
                            .text(String.format("fake %s", integer))
                            .code(integer)
                            .datetime(LocalDateTime.now())
                            .build())
                    .toArray(SampleDto[]::new);
        }
    }

    @Nested
    @DisplayName("POST -> save one")
    class SaveTest {

        @Test
        void GivenDto_WhenPost_ThenReturnIdOnCreatedStatus() {
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
        void GivenNullDto_WhenPost_ThenReturnUnsupportedMediaError() {
            final SampleDto givenDto = null;
            final var givenUri = uriBuilder.build().toUri();

            final var actual = systemUnderTest.exchange(givenUri, POST, new HttpEntity<>(givenDto), String.class);

            assertNotNull(actual);
            assertEquals(415, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void GivenDtoWithInvalidField_WhenPost_ThenReturnInternalServerError() {
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
            assertEquals(500, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
            assertFalse(actual.getBody().isEmpty());
        }
    }

    @Nested
    @DisplayName("POST -> save all")
    class SaveAllTest {

        @Test
        void GivenListOfDto_WhenPost_ThenReturnListOfIdOnCreatedStatus() {
            final var givenListOfDto = TestFixture.multiSample(2);
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
            assertEquals(2, actual.getBody().size());
        }

        @Test
        void GivenEmptyList_WhenPost_ThenReturnInternalServerError() {
            final var givenListOfDto = new SampleDto[0];
            final var givenUri = uriBuilder.path("/saveAll").build().toUri();

            final var actual = systemUnderTest.exchange(givenUri, POST, new HttpEntity<>(givenListOfDto), String.class);

            assertNotNull(actual);
            assertEquals(500, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void GivenNullList_WhenPost_ThenReturnUnsupportedMediaError() {
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
            listOfIdentities = sampleRepository.insertAll(TestFixture.multiSample(1));
        }

        @AfterEach
        void truncate() {
            sampleRepository.truncate();
        }

        @Test
        void GivenId_WhenGet_ThenReturnUniqueSampleDtoOnOkStatus() {
            final var givenId = TestFixtureUtils.selectByRandom(listOfIdentities);
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
        void GivenNullId_WhenGet_ThenReturnNotFoundError() {
            final Long givenId = null;
            final var givenUri = uriBuilder.path("/{id}").build(givenId);

            final var actual = systemUnderTest.exchange(givenUri, GET, new HttpEntity<>(null), String.class);

            assertNotNull(actual);
            assertEquals(404, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void GivenEmptyId_WhenGet_ThenReturnNotFoundError() {
            final String givenId = "";
            final var givenUri = uriBuilder.path("/{id}").build(givenId);

            final var actual = systemUnderTest.exchange(givenUri, GET, new HttpEntity<>(null), String.class);

            assertNotNull(actual);
            assertEquals(404, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void GivenNotNumberId_WhenGet_ThenReturnInternalServerError() {
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
            sampleRepository.insertAll(TestFixture.multiSample(2));
        }

        @AfterEach
        void truncate() {
            sampleRepository.truncate();
        }

        @Test
        void GivenNothing_WhenGet_ThenReturnListOfSampleDtoOnOkStatus() {
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
        void GivenIdSequence_WhenGet_ThenReturnListOfSampleDtoOnOkStatus() {
            var givenIdentities = sampleRepository.identities()
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
        void GivenNullAsIdSequence_WhenGet_ThenReturnNotFoundError() {
            final String givenIdentities = null;
            final var givenUri = uriBuilder.path("/findAll/{identities}").build(givenIdentities);

            final var actual = systemUnderTest.exchange(givenUri, GET, new HttpEntity<>(null), String.class);

            assertNotNull(actual);
            assertEquals(404, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void GivenEmptyStringAsIdSequence_WhenGet_ThenReturnNotFoundError() {
            final var givenIdentities = "";
            final var givenUri = uriBuilder.path("/findAll/{identities}").build(givenIdentities);

            final var actual = systemUnderTest.exchange(givenUri, GET, new HttpEntity<>(null), String.class);

            assertNotNull(actual);
            assertEquals(404, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void GivenIdSequenceWithNotNumber_WhenGet_ThenReturnInternalServerError() {
            final var givenIdentities = "not number";
            final var givenUri = uriBuilder.path("/findAll/{identities}").build(givenIdentities);

            final var actual = systemUnderTest.exchange(givenUri, GET, new HttpEntity<>(null), String.class);

            assertNotNull(actual);
            assertEquals(500, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void GivenIdSequenceWithNullPlace_WhenGet_ThenReturnInternalServerError() {
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
            listOfIdentities = sampleRepository.insertAll(TestFixture.multiSample(1));
        }

        @AfterEach
        void truncate() {
            sampleRepository.truncate();
        }

        @Test
        void GivenDtoAndId_WhenPut_ThenReturnNothingOnNoContentStatus() {
            final var givenId = TestFixtureUtils.selectByRandom(listOfIdentities);
            final var givenUri = uriBuilder.path("/{id}").build(givenId);
            final var givenDto = SampleDto.builder()
                    .from(sampleRepository.selectById(givenId).orElseThrow())
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
        void GivenNullDtoAndId_WhenPut_ThenReturnInternalServerError() {
            final var givenId = TestFixtureUtils.selectByRandom(listOfIdentities);
            final var givenUri = uriBuilder.path("/{id}").build(givenId);
            final SampleDto givenDto = null;

            final var actual = systemUnderTest.exchange(givenUri, PUT, new HttpEntity<>(givenDto), String.class);

            assertNotNull(actual);
            assertEquals(500, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void GivenEmptyDtoAndId_WhenPut_ThenReturnInternalServerError() {
            final var givenId = TestFixtureUtils.selectByRandom(listOfIdentities);
            final var givenUri = uriBuilder.path("/{id}").build(givenId);
            final var givenDto = SampleDto.builder().build();

            final var actual = systemUnderTest.exchange(
                    givenUri,
                    PUT,
                    new HttpEntity<>(givenDto),
                    new ParameterizedTypeReference<List<String>>() {
                    });

            assertNotNull(actual);
            assertEquals(500, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void GivenDtoAndNullId_WhenPut_ThenReturnNotFoundError() {
            final Long givenId = null;
            final var givenUri = uriBuilder.path("/{id}").build(givenId);
            final var givenDto = SampleDto.builder().build();

            final var actual = systemUnderTest.exchange(givenUri, PUT, new HttpEntity<>(givenDto), String.class);

            assertNotNull(actual);
            assertEquals(404, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void GivenDtoAndEmptyId_WhenPut_ThenReturnNotFoundError() {
            final var givenId = "";
            final var givenUri = uriBuilder.path("/{id}").build(givenId);
            final var givenDto = SampleDto.builder().build();

            final var actual = systemUnderTest.exchange(givenUri, PUT, new HttpEntity<>(givenDto), String.class);

            assertNotNull(actual);
            assertEquals(404, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void GivenDtoAndNotNumberId_WhenPut_ThenReturnInternalServerError() {
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
            listOfIdentities = sampleRepository.insertAll(TestFixture.multiSample(1));
        }

        @AfterEach
        void truncate() {
            sampleRepository.truncate();
        }

        @Test
        void GivenId_WhenDelete_ThenReturnNothingOnNoContentStatus() {
            final var givenId = TestFixtureUtils.selectByRandom(listOfIdentities);
            final var givenUri = uriBuilder.path("/{id}").build(givenId);

            final var actual = systemUnderTest.exchange(givenUri, DELETE, new HttpEntity<>(null), Void.class);

            assertNotNull(actual);
            assertEquals(204, actual.getStatusCode().value());
            assertNull(actual.getBody());
        }

        @Test
        void GivenNullId_WhenDelete_ThenReturnInternalServerError() {
            final Long givenId = null;
            final var givenUri = uriBuilder.path("/{id}").build(givenId);

            final var actual = systemUnderTest.exchange(givenUri, DELETE, new HttpEntity<>(null), String.class);

            assertNotNull(actual);
            assertEquals(404, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void GivenEmptyId_WhenDelete_ThenReturnInternalServerError() {
            final var givenId = "";
            final var givenUri = uriBuilder.path("/{id}").build(givenId);

            final var actual = systemUnderTest.exchange(givenUri, DELETE, new HttpEntity<>(null), String.class);

            assertNotNull(actual);
            assertEquals(404, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void GivenNotNumberId_WhenDelete_ThenReturnInternalServerError() {
            final var givenId = "not number";
            final var givenUri = uriBuilder.path("/{id}").build(givenId);

            final var actual = systemUnderTest.exchange(givenUri, DELETE, new HttpEntity<>(null), String.class);

            assertNotNull(actual);
            assertEquals(500, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }
    }

    @Nested
    @DisplayName("GET -> delete all")
    class DeleteAllTest {

        @BeforeEach
        void populate() {
            sampleRepository.insertAll(TestFixture.multiSample(3));
        }

        @AfterEach
        void truncate() {
            sampleRepository.truncate();
        }

        @Test
        void GivenNothing_WhenDelete_ThenReturnNothingOnNoContentStatus() {
            final var givenUri = uriBuilder.path("/truncate").build().toUri();

            final var actual = systemUnderTest.exchange(givenUri, DELETE, new HttpEntity<>(null), Void.class);

            assertNotNull(actual);
            assertEquals(204, actual.getStatusCode().value());
        }

        @Test
        void GivenIdSequence_WhenDelete_ThenReturnNothingOnNoContentStatus() {
            var givenIdentities = sampleRepository.identities()
                    .stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            final var givenUri = uriBuilder.path("/deleteAll/{identities}").build(givenIdentities);

            final var actual = systemUnderTest.exchange(givenUri, DELETE, new HttpEntity<>(null), Void.class);

            assertNotNull(actual);
            assertEquals(204, actual.getStatusCode().value());
        }

        @Test
        void GivenNullAsIdSequence_WhenDelete_ThenReturnNotFoundError() {
            final String givenIdentities = null;
            final var givenUri = uriBuilder.path("/deleteAll/{identities}").build(givenIdentities);

            final var actual = systemUnderTest.exchange(givenUri, DELETE, new HttpEntity<>(null), String.class);

            assertNotNull(actual);
            assertEquals(404, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void GivenEmptyStringAsIdSequence_WhenDelete_ThenReturnNotFoundError() {
            final var givenIdentities = "";
            final var givenUri = uriBuilder.path("/deleteAll/{identities}").build(givenIdentities);

            final var actual = systemUnderTest.exchange(givenUri, DELETE, new HttpEntity<>(null), String.class);

            assertNotNull(actual);
            assertEquals(404, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void GivenIdSequenceWithNotNumber_WhenDelete_ThenReturnInternalServerError() {
            final var givenIdentities = "not number";
            final var givenUri = uriBuilder.path("/deleteAll/{identities}").build(givenIdentities);

            final var actual = systemUnderTest.exchange(givenUri, DELETE, new HttpEntity<>(null), String.class);

            assertNotNull(actual);
            assertEquals(500, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }

        @Test
        void GivenIdSequenceWithNullPlace_WhenDelete_ThenReturnNotFoundError() {
            final var givenIdentities = "1,,2";
            final var givenUri = uriBuilder.path("/deleteAll/{identities}").build(givenIdentities);

            final var actual = systemUnderTest.exchange(givenUri, DELETE, new HttpEntity<>(null), String.class);

            assertNotNull(actual);
            assertEquals(500, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
        }
    }
}
