
mvn -f hello-world/pom.xml clean package
mvn -f profile/pom.xml clean package
mvn -f properties/pom.xml clean package
mvn -f logger-log4j2-console/pom.xml clean package
mvn -f logger-log4j2-file/pom.xml clean package
mvn -f logger-log4j2-database/pom.xml clean package
mvn -f event-handling/pom.xml clean package
mvn -f rdbms-h2/pom.xml clean package
mvn -f rdbms-mysql/pom.xml clean package
mvn -f rdbms-postgresql/pom.xml clean package
mvn -f rdbms-oracle/pom.xml clean package
mvn -f nosql-redis/pom.xml clean package
mvn -f nosql-redis-reactive/pom.xml clean package
mvn -f nosql-mongodb/pom.xml clean package
mvn -f nosql-mongodb-reactive/pom.xml clean package
mvn -f messaging-apache-artemis-mq/pom.xml clean package
mvn -f messaging-rabbit-mq/pom.xml clean package
mvn -f messaging-apache-kafka/pom.xml clean package
mvn -f messaging-pulsar/pom.xml clean package
mvn -f messaging-redis/pom.xml clean package
mvn -f streaming-apache-kafka/pom.xml clean package
mvn -f cdc-debezium/pom.xml clean package
mvn -f cdc-embedded-debezium/pom.xml clean package
mvn -f log-aggregation-elk/pom.xml clean package
mvn -f observability-grafana-stack/pom.xml clean package
mvn -f observability-jaeger/pom.xml clean package
mvn -f restful-web-api/pom.xml clean package
mvn -f security-rbac-inmemory/pom.xml clean package
mvn -f security-rbac-jwt/pom.xml clean package