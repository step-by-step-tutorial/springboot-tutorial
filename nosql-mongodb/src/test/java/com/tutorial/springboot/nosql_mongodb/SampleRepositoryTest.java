package com.tutorial.springboot.nosql_mongodb;

import com.tutorial.springboot.nosql_mongodb.document.SampleDocument;
import com.tutorial.springboot.nosql_mongodb.repository.SampleRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataMongoTest
@ActiveProfiles("test")
@DisplayName("unit tests of mongodb sample repository")
class SampleRepositoryTest {

    @Container
    static final MongoDBContainer MONGODB = new MongoDBContainer("mongo:5.0.16");

    @Autowired
    SampleRepository systemUnderTest;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", MONGODB::getReplicaSetUrl);
    }

    @BeforeAll
    static void setUp() {
        MONGODB.start();
    }

    @AfterAll
    static void tearDown() {
        MONGODB.stop();
    }

    /**
     * This class includes Stubs data.
     */
    static class StubFixturesFactory {
        static final LocalDateTime NOW = LocalDateTime.now()
                .truncatedTo(ChronoUnit.SECONDS);
        static final LocalDateTime TOMORROW = LocalDateTime.now()
                .plusDays(1)
                .truncatedTo(ChronoUnit.SECONDS);
        static final SampleDocument SAMPLE_DOCUMENT = SampleDocument.create()
                .setName("name")
                .setCode(1)
                .setDatetime(NOW);
    }

    @Nested
    @DisplayName("nested unit tests of save")
    class SaveTest {

        @BeforeEach
        void setUp() {
            assertNotNull(systemUnderTest);
        }

        @Test
        @DisplayName("save a document when there is no exception")
        void shouldReturnDocumentWithIdBySuccessfulSave() {
            final var givenDocument = StubFixturesFactory.SAMPLE_DOCUMENT;

            final var actual = systemUnderTest.save(givenDocument);

            assertNotNull(actual);
            assertNotNull(actual.getId());
        }
    }

    @Nested
    @DisplayName("nested unit tests of find")
    class FindTest {

        String id;

        @BeforeEach
        void setUp() {
            assertNotNull(systemUnderTest);
            this.id = systemUnderTest.save(StubFixturesFactory.SAMPLE_DOCUMENT).getId();
        }

        @Test
        @DisplayName("find one document by given Id")
        void shouldReturnModelByGivenId() {
            final var givenId = id;

            final var expectedName = "name";
            final var expectedCode = 1;
            final var expectedDatetime = StubFixturesFactory.NOW;

            final var actual = systemUnderTest.findById(givenId);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            actual.ifPresent(document -> {
                assertEquals(expectedName, document.getName());
                assertEquals(expectedCode, document.getCode());
                assertEquals(expectedDatetime, document.getDatetime());
            });
        }
    }
}
