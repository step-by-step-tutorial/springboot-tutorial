package com.tutorial.springboot.h2.repository;

import com.tutorial.springboot.h2.domain.SampleEntity;
import org.springframework.data.repository.CrudRepository;

public interface SampleRepository extends CrudRepository<SampleEntity, Long> {
}
