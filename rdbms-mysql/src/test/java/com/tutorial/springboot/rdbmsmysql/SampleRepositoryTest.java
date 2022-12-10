package com.tutorial.springboot.rdbmsmysql;

import com.tutorial.springboot.rdbmsmysql.domain.SampleEntity;
import com.tutorial.springboot.rdbmsmysql.repository.SampleRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles({"test"})
@DisplayName("Sample Repository Tests")
class SampleRepositoryTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("springboot_tutorial")
            .withUsername("user")
            .withPassword("password");
    ;
    @Autowired
    private SampleRepository underTest;

    @DynamicPropertySource
    static void registerMySQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.datasource.driver-class-name", mysql::getDriverClassName);
    }

    @BeforeAll
    static void beforeAll() {
        mysql.start();
    }

    @AfterAll
    static void afterAll() {
        mysql.stop();
    }

    static class Stub {
        static SampleEntity SAMPLE = SampleEntity.create()
                .setName("test")
                .setCode(1)
                .setDatetime(LocalDateTime.now());
    }

    @Nested
    @DisplayName("save nested tests")
    class SaveTest {
        @Test
        @DisplayName("save an entity")
        void givenEntityWhenInvokeSaveThenReturnsPersistedEntity() {
            var givenEntity = Stub.SAMPLE;

            var result = underTest.save(givenEntity);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals(Stub.SAMPLE.getName(), result.getName());
            assertEquals(Stub.SAMPLE.getCode(), result.getCode());
            assertEquals(Stub.SAMPLE.getDatetime(), result.getDatetime());
        }
    }

    @Nested
    @DisplayName("find nested tests")
    class FindTest {

        @BeforeEach
        void initDatabase() {
            underTest.save(Stub.SAMPLE);
        }

        @Test
        @DisplayName("find one entity by ID")
        void givenIdWhenInvokeFindByIdThenReturnsPersistedEntity() {

            var givenId = 1L;

            var result = underTest.findById(givenId);

            assertTrue(result.isPresent());
            result.ifPresent(entity -> {
                assertEquals(Stub.SAMPLE.getName(), entity.getName());
                assertEquals(Stub.SAMPLE.getCode(), entity.getCode());
                assertEquals(Stub.SAMPLE.getDatetime(), entity.getDatetime());
            });
        }
    }

    @Nested
    @DisplayName("update nested tests")
    class UpdateTest {

        @BeforeEach
        void initDatabase() {
            underTest.save(Stub.SAMPLE);
        }

        @Test
        @DisplayName("update one entity by new values")
        void givenEntityWhenTransactionIsClosedThenTupleWillBeUpdated() {
            var givenId = 1L;
            var givenEntity = underTest.findById(givenId);

            givenEntity.ifPresent(entity -> {
                entity.setName("updated_test");
                entity.setCode(2);
            });

            assertTrue(true);
        }

        @AfterEach
        void assertionCheck() {
            var givenId = 1L;
            var result = underTest.findById(givenId);

            assertTrue(result.isPresent());
            result.ifPresent(entity -> {
                assertEquals("updated_test", entity.getName());
                assertEquals(2, entity.getCode());
                assertEquals(Stub.SAMPLE.getDatetime(), entity.getDatetime());
            });
        }
    }

    @Nested
    @DisplayName("delete nested tests")
    class DeleteTest {

        @BeforeEach
        void initDatabase() {
            underTest.save(Stub.SAMPLE);
        }

        @Test
        @DisplayName("delete one entity by ID")
        void givenIdWhenInvokeDeleteByIdThenTupleWillBeDeletedFromDatabase() {
            var givenId = 1L;
            underTest.deleteById(givenId);
            assertTrue(true);
        }

        @AfterEach
        void assertionCheck() {
            var givenId = 1L;
            var result = underTest.findById(givenId);
            assertTrue(result.isEmpty());
        }
    }
}
