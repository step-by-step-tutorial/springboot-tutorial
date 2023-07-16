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
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataMongoTest
@ActiveProfiles("test")
@DisplayName("unit tests of sample repository")
class SampleRepositoryTest {

    @Container
    static final MongoDBContainer mongoDbContainer = new MongoDBContainer("mongo:5.0.16");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDbContainer::getReplicaSetUrl);
    }

    @BeforeAll
    static void setUp() {
        mongoDbContainer.start();
    }

    @AfterAll
    static void tearDown() {
        mongoDbContainer.stop();
    }

    @Autowired
    SampleRepository systemUnderTest;

    /**
     * This class includes Stubs data.
     */
    static class StubFactory {
        static final LocalDateTime NOW = LocalDateTime.now()
                .truncatedTo(ChronoUnit.SECONDS);
        static final LocalDateTime TOMORROW = LocalDateTime.now()
                .plusDays(1)
                .truncatedTo(ChronoUnit.SECONDS);
        static final SampleDocument SAMPLE_DOCUMENT = SampleDocument.create()
                .setName("stub name")
                .setCode(1)
                .setDatetime(NOW);
    }

    /**
     * This method executes final-all query then return all available IDs as a{@link String} array and if there is no
     * persisted documents then throws {@link NoSuchElementException}.
     *
     * @return return all available IDs as a{@link String} array
     * @throws NoSuchElementException if there is no persisted document
     */
    String[] giveMeId() {
        return systemUnderTest.findAll()
                .stream()
                .map(SampleDocument::getId)
                .toArray(String[]::new);
    }

    @Nested
    @DisplayName("nested unit tests of save")
    class SaveTest {

        @BeforeEach
        void preCondition() {
            assertNotNull(systemUnderTest);
        }

        @Test
        @DisplayName("save a document")
        void shouldReturnDocumentWithIdBySuccessfulSave() {
            final var givenDocument = StubFactory.SAMPLE_DOCUMENT;

            final var actual = systemUnderTest.save(givenDocument);

            assertNotNull(actual);
            assertNotNull(actual.getId());
        }
    }

    @Nested
    @DisplayName("nested unit tests of find")
    class FindTest {

        @BeforeEach
        void initDatabase() {
            assertNotNull(systemUnderTest);
            systemUnderTest.save(StubFactory.SAMPLE_DOCUMENT);
        }

        @Test
        @DisplayName("find one document by Id")
        void shouldReturnModelByGivenId() {
            var givenId = giveMeId()[0];

            final var expectedName = "stub name";
            final var expectedCode = 1;
            final var expectedDatetime = StubFactory.NOW;

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
