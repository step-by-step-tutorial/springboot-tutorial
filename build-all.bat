call mvn -f hello-world/pom.xml clean package
call mvn -f profile/pom.xml clean package
call mvn -f properties/pom.xml clean package
call mvn -f logger-log4j2-console/pom.xml clean package
call mvn -f logger-log4j2-file/pom.xml clean package
call mvn -f logger-log4j2-database/pom.xml clean package
call mvn -f event-processor/pom.xml clean package
call mvn -f rdbms-h2/pom.xml clean package
call mvn -f rdbms-mysql/pom.xml clean package
call mvn -f rdbms-postgresql/pom.xml clean package
call mvn -f rdbms-oracle/pom.xml clean package
call mvn -f nosql-redis/pom.xml clean package
call mvn -f nosql-redis-reactive/pom.xml clean package
call mvn -f nosql-mongodb/pom.xml clean package
call mvn -f nosql-mongodb-reactive/pom.xml clean package
call mvn -f messaging-apache-artemis-mq/pom.xml clean package
call mvn -f messaging-rabbit-mq/pom.xml clean package
call mvn -f messaging-apache-kafka/pom.xml clean package
call mvn -f restful-web-api/pom.xml clean package
