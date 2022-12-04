package com.tutorial.springboot.h2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tutorial.springboot.h2.domain.DomainEntity;
import com.tutorial.springboot.h2.repository.DomainRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("Domain Repository Tests")
class DomainRepositoryTest {

  @Autowired
  private DomainRepository underTest;

  @Nested
  @DisplayName("save nested tests")
  class SaveTest {
    @Test
    @DisplayName("save an entity")
    void givenEntityWhenInvokeSaveThenReturnPersistedEntity() {
      var givenEntity = DomainEntity.create()
          .setName("test")
          .setAge(18)
          .setEmail("test@gmail.com");

      var result = underTest.save(givenEntity);

      assertNotNull(result);
      assertEquals(1L, result.getId());
      assertEquals(givenEntity.getName(), result.getName());
      assertEquals(givenEntity.getAge(), result.getAge());
      assertEquals(givenEntity.getEmail(), result.getEmail());
    }
  }

  @Nested
  @DisplayName("find nested tests")
  class FindTest {

    @BeforeEach
    void initDatabase() {
      underTest.save(DomainEntity.create()
          .setName("test")
          .setAge(18)
          .setEmail("test@gmail.com"));
    }

    @Test
    @DisplayName("find one entity by ID")
    void givenIdWhenInvokeFindByIdThenReturnPersistedEntity() {

      var givenId = 1L;

      var result = underTest.findById(givenId);

      assertTrue(result.isPresent());
      result.ifPresent(entity -> {
        assertEquals("test", entity.getName());
        assertEquals(18, entity.getAge());
        assertEquals("test@gmail.com", entity.getEmail());
      });
    }
  }

  @Nested
  @DisplayName("update nested tests")
  class UpdateTest {

    @BeforeEach
    void initDatabase() {
      underTest.save(DomainEntity.create()
          .setName("test")
          .setAge(18)
          .setEmail("test@gmail.com"));
    }

    @Test
    @DisplayName("update one entity by new values")
    void givenEntityWhenTransactionIsClosedThenEntityWillBeUpdated() {
      var givenId = 1L;
      var givenEntity = underTest.findById(givenId);

      givenEntity.ifPresent(entity -> {
        entity.setName("updated_test");
        entity.setAge(20);
      });

      assertTrue(true);
    }

    @AfterEach
    void check() {
      var givenId = 1L;
      var result = underTest.findById(givenId);

      assertTrue(result.isPresent());
      result.ifPresent(entity -> {
        assertEquals("updated_test", entity.getName());
        assertEquals(20, entity.getAge());
        assertEquals("test@gmail.com", entity.getEmail());
      });
    }
  }

  @Nested
  @DisplayName("delete nested tests")
  class DeleteTest {

    @BeforeEach
    void initDatabase() {
      underTest.save(DomainEntity.create()
          .setName("test")
          .setAge(18)
          .setEmail("test@gmail.com"));
    }

    @Test
    @DisplayName("delete one entity by ID")
    void givenIdWhenInvokeDeleteByIdThenWillBeDeletedFromDatabase() {
      var givenId = 1L;

      underTest.deleteById(givenId);

      assertTrue(true);
    }

    @AfterEach
    void check() {
      var givenId = 1L;
      var result = underTest.findById(givenId);
      assertTrue(result.isEmpty());
    }
  }
}
