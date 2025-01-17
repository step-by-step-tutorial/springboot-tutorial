package com.tutorial.springboot.cdcdebezium.repository;

import com.tutorial.springboot.cdcdebezium.entity.Example;
import org.springframework.data.repository.CrudRepository;

public interface JdbcExampleRepository extends CrudRepository<Example, Long> {
}
