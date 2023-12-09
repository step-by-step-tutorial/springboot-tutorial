package com.tutorial.springboot.rest_basic.api;

import com.tutorial.springboot.rest_basic.TestDbUtils;
import com.tutorial.springboot.rest_basic.dto.SampleDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import java.time.LocalDateTime;
import java.util.List;

import static com.tutorial.springboot.rest_basic.TestApiUtils.generateTestUri;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayName("Sample API test")
class SampleApiTest {

    static final String BASE_PATH = "/v1/samples";

    @Autowired
    TestRestTemplate restTemplate;

    @LocalServerPort
    int port;

    @Nested
    @DisplayName("POST -> " + BASE_PATH)
    class SaveTest {

        @Test
        void GivenDto_WhenPost_ThenReturnIdOnCreatedStatus() {
            final var givenDto = SampleDto.builder().text("fake").code(1).datetime(LocalDateTime.now()).build();
            final var givenUri = generateTestUri(port).path(BASE_PATH).build().toUri();

            final var actual = restTemplate.exchange(givenUri, HttpMethod.POST, new HttpEntity<>(givenDto), Long.class);

            assertNotNull(actual);
            assertEquals(201, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
            assertNotNull(actual.getHeaders().getLocation());
            assertEquals(givenUri.toString() + "/" + actual.getBody(), actual.getHeaders().getLocation().toString());
        }
    }

    @Nested
    @DisplayName("GET -> " + BASE_PATH + "/{id}")
    class FindByIdTest {

        @BeforeEach
        void setUp() {
            TestDbUtils.SampleCollection.populate();
        }

        @AfterEach
        void tearDown() {
            TestDbUtils.SampleCollection.truncate();
        }

        @Test
        void GivenId_WhenGet_ThenReturnUniqueSampleDtoOnOkStatus() {
            final var givenId = TestDbUtils.SampleCollection.selectAll().stream().findFirst().orElseThrow();
            final var givenUri = generateTestUri(port).path(BASE_PATH).path("/{id}").build(givenId);

            final var actual = restTemplate.exchange(givenUri, HttpMethod.GET, new HttpEntity<>(null), SampleDto.class);

            assertNotNull(actual);
            assertEquals(200, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
            assertNotNull(actual.getBody().text());
            assertNotNull(actual.getBody().code());
            assertNotNull(actual.getBody().datetime());
        }
    }

    @Nested
    @DisplayName("GET -> " + BASE_PATH)
    class FindAllTest {

        @BeforeEach
        void setUp() {
            TestDbUtils.SampleCollection.populate();
        }

        @AfterEach
        void tearDown() {
            TestDbUtils.SampleCollection.truncate();
        }

        @Test
        void GivenNothing_WhenGet_ThenReturnListOfSampleDtoOnOkStatus() {
            final var givenUri = generateTestUri(port).path(BASE_PATH).build().toUri();

            final var actual = restTemplate.exchange(givenUri, HttpMethod.GET, new HttpEntity<>(null), List.class);

            assertNotNull(actual);
            assertEquals(200, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
            assertTrue(actual.getBody().size() > 1);
        }
    }

    @Nested
    @DisplayName("PUT -> " + BASE_PATH + "/{id}")
    class UpdateTest {

        @BeforeEach
        void setUp() {
            TestDbUtils.SampleCollection.populate();
        }

        @AfterEach
        void tearDown() {
            TestDbUtils.SampleCollection.truncate();
        }

        @Test
        void GivenDtoAndId_WhenPut_ThenReturnNothingOnNoContentStatus() {
            final var givenId = TestDbUtils.SampleCollection.selectAll().stream().findFirst().orElseThrow();
            final var givenDto = SampleDto.builder().from(TestDbUtils.SampleCollection.select(givenId)).text("update").code(1).datetime(LocalDateTime.now()).build();
            final var givenUri = generateTestUri(port).path(BASE_PATH).path("/{id}").build(givenId);

            final var actual = restTemplate.exchange(givenUri, HttpMethod.PUT, new HttpEntity<>(givenDto), Void.class);

            assertNotNull(actual);
            assertEquals(204, actual.getStatusCode().value());
            assertNull(actual.getBody());
        }
    }

}
