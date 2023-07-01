package com.tutorial.springboot.nosql_mongodb_reactive.repository;

import com.tutorial.springboot.nosql_mongodb_reactive.document.SampleDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface SampleRepository extends ReactiveMongoRepository<SampleDocument, String> {
}
