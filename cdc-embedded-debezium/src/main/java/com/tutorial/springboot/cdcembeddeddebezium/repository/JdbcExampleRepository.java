package com.tutorial.springboot.cdcembeddeddebezium.repository;

import com.tutorial.springboot.cdcembeddeddebezium.entity.Example;
import org.springframework.data.repository.CrudRepository;

public interface JdbcExampleRepository extends CrudRepository<Example, Long> {
}
