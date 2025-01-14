package com.tutorial.springboot.cdcdebezium.repository;

import com.tutorial.springboot.cdcdebezium.entity.SampleEntity;
import org.springframework.data.repository.CrudRepository;

public interface JdbcSampleRepository extends CrudRepository<SampleEntity, Long> {
}
