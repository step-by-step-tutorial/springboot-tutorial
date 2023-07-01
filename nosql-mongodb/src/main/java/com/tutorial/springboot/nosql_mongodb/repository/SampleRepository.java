package com.tutorial.springboot.nosql_mongodb.repository;

import com.tutorial.springboot.nosql_mongodb.document.SampleDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SampleRepository extends MongoRepository<SampleDocument, String> {
}
