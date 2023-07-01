package com.tutorial.springboot.rdbms_postgresql.repository;

import com.tutorial.springboot.rdbms_postgresql.entity.SampleEntity;
import org.springframework.data.repository.CrudRepository;

public interface SampleRepository extends CrudRepository<SampleEntity, Long> {
}
