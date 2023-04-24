package com.tutorial.springboot.nosqlmongodb.repository;

import com.tutorial.springboot.nosqlmongodb.document.SampleDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SampleRepository extends MongoRepository<SampleDocument, String> {
}
