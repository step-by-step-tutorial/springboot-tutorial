package com.tutorial.springboot.nosqlmongodbreactive.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "com.tutorial.springboot.nosqlmongodbrepository.repository")
public class MongoDbConfig {
}
