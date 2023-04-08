package com.tutorial.springboot.rdbmspostgresql.repository;

import com.tutorial.springboot.rdbmspostgresql.domain.SampleEntity;
import org.springframework.data.repository.CrudRepository;

public interface SampleRepository extends CrudRepository<SampleEntity, Long> {
}
