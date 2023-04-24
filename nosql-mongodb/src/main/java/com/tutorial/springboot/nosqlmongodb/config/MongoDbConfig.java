package com.tutorial.springboot.nosqlmongodb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.tutorial.springboot.nosqlmongodb.repository")
public class MongoDbConfig {
}
