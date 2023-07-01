package com.tutorial.springboot.rdbm_mysql.repository;

import com.tutorial.springboot.rdbm_mysql.entity.SampleEntity;
import org.springframework.data.repository.CrudRepository;

public interface SampleRepository extends CrudRepository<SampleEntity, Long> {
}
