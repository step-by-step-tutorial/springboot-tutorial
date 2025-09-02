package com.tutorial.springboot.newsqlcockroachdb.repository;

import com.tutorial.springboot.newsqlcockroachdb.entity.SampleEntity;
import org.springframework.data.repository.CrudRepository;

public interface SampleRepository extends CrudRepository<SampleEntity, Long> {
}
