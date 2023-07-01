package com.tutorial.springboot.rdbms_oracle.repository;

import com.tutorial.springboot.rdbms_oracle.entity.SampleEntity;
import org.springframework.data.repository.CrudRepository;

public interface SampleRepository extends CrudRepository<SampleEntity, Long> {
}
