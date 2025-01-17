package com.tutorial.springboot.cdcdebezium.model;

public record JdbcUrlModel(String host, String port, String dbName) {
}