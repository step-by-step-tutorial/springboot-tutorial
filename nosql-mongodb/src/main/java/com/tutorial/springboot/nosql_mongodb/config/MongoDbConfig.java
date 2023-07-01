package com.tutorial.springboot.nosql_mongodb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.tutorial.springboot.nosql_mongodb.repository")
public class MongoDbConfig {
}
