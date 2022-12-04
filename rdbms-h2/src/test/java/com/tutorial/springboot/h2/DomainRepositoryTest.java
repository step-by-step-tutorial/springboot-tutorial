package com.tutorial.springboot.h2;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.tutorial.springboot.h2.domain.DomainEntity;
import com.tutorial.springboot.h2.repository.DomainRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Commit
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@DisplayName("DomainRepository Tests")
class DomainRepositoryTest {

  private final Logger logger = LoggerFactory.getLogger(DomainRepositoryTest.class.getSimpleName());

  @Autowired
  private DomainRepository underTest;

  @Nested
  class SaveTest {
    @Test
    @DisplayName("save domain entity")
    void givenEntityWhenSaveThenReturnPersistedEntity() {
      DomainEntity givenEntity = DomainEntity.create()
          .setName("test")
          .setAge(18)
          .setEmail("test@gmail.com");

      DomainEntity result = underTest.save(givenEntity);

      assertNotNull(result);
      assertNotNull(result.getId());
      assertEquals(givenEntity.getName(), result.getName());
      assertEquals(givenEntity.getAge(), result.getAge());
      assertEquals(givenEntity.getEmail(), result.getEmail());

      logger.info(String.format("%s", givenEntity));
    }
  }

}
