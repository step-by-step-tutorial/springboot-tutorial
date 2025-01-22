package com.tutorial.springboot.cdcembeddeddebezium.dto;

public record JdbcUrlModel(String host, String port, String dbName) {
}