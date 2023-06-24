package com.tutorial.springboot.nosqlmongodbreactive.repository;

import com.tutorial.springboot.nosqlmongodbreactive.document.SampleDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface SampleRepository extends ReactiveMongoRepository<SampleDocument, String> {
}
