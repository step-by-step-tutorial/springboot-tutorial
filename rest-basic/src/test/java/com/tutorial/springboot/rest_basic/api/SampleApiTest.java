package com.tutorial.springboot.rest_basic.api;

import com.tutorial.springboot.rest_basic.dto.SampleDto;
import com.tutorial.springboot.rest_basic.dto.Storage;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;

import java.time.LocalDateTime;

import static com.tutorial.springboot.rest_basic.TestApiUtils.createUriBuilder;
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
    @DisplayName("sending POST request to -> " + BASE_PATH)
    class SaveTest {

        @Test
        void GivenDto_ThenReturnId() {
            final var givenDto = new SampleDto(null, "text", 1, LocalDateTime.now());
            final var givenUri = createUriBuilder(port)
                    .path(BASE_PATH)
                    .build()
                    .toUri();

            final var actual = restTemplate.postForEntity(givenUri, new HttpEntity<>(givenDto), Long.class);

            assertNotNull(actual);
            assertEquals(201, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
            assertNotNull(actual.getHeaders().getLocation());
            assertEquals(givenUri.toString() + "/" + actual.getBody(), actual.getHeaders().getLocation().toString());
        }
    }

    @Nested
    @DisplayName("sending GET request to -> " + BASE_PATH + "/{id}")
    class FindByIdTest {
        final LocalDateTime now = LocalDateTime.now();

        @BeforeEach
        void setUp() {
            Storage.SAMPLE_COLLECTION.put(1L, new SampleDto(1L, "text", 1, now));
        }

        @AfterEach
        void tearDown() {
            Storage.SAMPLE_COLLECTION.clear();
        }

        @Test
        void GivenId_ThenReturnUniqueSampleDto() {
            final var givenId = 1L;
            final var givenUri = createUriBuilder(port)
                    .path(BASE_PATH)
                    .path("/{id}")
                    .build(givenId);

            final var actual = restTemplate.getForEntity(givenUri, SampleDto.class);

            assertNotNull(actual);
            assertEquals(200, actual.getStatusCode().value());
            assertNotNull(actual.getBody());
            assertEquals("text", actual.getBody().text());
            assertEquals(1, actual.getBody().code());
            assertEquals(now, actual.getBody().datetime());
        }
    }

}
