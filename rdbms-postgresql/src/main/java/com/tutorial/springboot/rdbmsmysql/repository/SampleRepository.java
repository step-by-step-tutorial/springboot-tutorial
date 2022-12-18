package com.tutorial.springboot.rdbmsmysql.repository;

import com.tutorial.springboot.rdbmsmysql.domain.SampleEntity;
import org.springframework.data.repository.CrudRepository;

public interface SampleRepository extends CrudRepository<SampleEntity, Long> {
}
