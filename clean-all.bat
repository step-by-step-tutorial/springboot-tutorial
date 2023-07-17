call mvn -f hello-world/pom.xml clean
call mvn -f profile/pom.xml clean
call mvn -f properties/pom.xml clean
mvn -f logger-log4j-console/pom.xml clean
mvn -f logger-log4j-file/pom.xml clean
mvn -f logger-log4j-database/pom.xml clean
call mvn -f event-processor/pom.xml clean
call mvn -f rdbms-h2/pom.xml clean
call mvn -f rdbms-mysql/pom.xml clean
call mvn -f rdbms-postgresql/pom.xml clean
call mvn -f rdbms-oracle/pom.xml clean
call mvn -f nosql-redis/pom.xml clean
call mvn -f nosql-redis-reactive/pom.xml clean
call mvn -f nosql-mongodb/pom.xml clean
call mvn -f nosql-mongodb-reactive/pom.xml clean
call mvn -f messaging-apache-artemis-mq/pom.xml clean
call mvn -f messaging-rabbit-mq/pom.xml clean
call mvn -f messaging-kafka/pom.xml clean
