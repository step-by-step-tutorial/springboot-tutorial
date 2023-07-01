package com.tutorial.springboot.nosql_mongodb_reactive.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "com.tutorial.springboot.nosql_mongodb_repository.repository")
public class MongoDbConfig {
}
