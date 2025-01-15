package com.tutorial.springboot.cdcdebezium.model;

public record JdbcUrlParts(String host, String port, String dbName) {
}