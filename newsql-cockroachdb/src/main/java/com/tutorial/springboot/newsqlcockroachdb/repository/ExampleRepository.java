package com.tutorial.springboot.newsqlcockroachdb.repository;

import com.tutorial.springboot.newsqlcockroachdb.entity.ExampleEntity;
import org.springframework.data.repository.CrudRepository;

public interface ExampleRepository extends CrudRepository<ExampleEntity, Long> {
}
