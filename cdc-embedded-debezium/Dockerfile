FROM eclipse-temurin:21-jdk-alpine

ARG JAR_PATH=./target
ARG JAR_NAME=cdc-embedded-debezium
ARG JAR_VERSION=0.0.1-SNAPSHOT
ARG TARGET_PATH=/app

RUN apk --no-cache add ca-certificates wget curl

ENV APPLICATION=${TARGET_PATH}/application.jar
ENV APP_HOST=0.0.0.0
ENV APP_PORT=8080
ENV APP_PROFILES=mysql,kafka,debezium
ENV DATABASE_USERNAME=user
ENV DATABASE_PASSWORD=password
ENV DATABASE_HOST=mysql
ENV DATABASE_PORT=3306
ENV DATABASE_NAME=tutorial_db
ENV KAFKA_TOPIC_NAME=cdc.tutorial_db.example_table
ENV KAFKA_GROUP_ID=cdc.tutorial_db.main-group
ENV KAFKA_URL=kafka:9093
ENV TOPIC_PREFIX=cdc
ENV TABLES=tutorial_db.example_table

ADD ${JAR_PATH}/${JAR_NAME}-${JAR_VERSION}.jar ${TARGET_PATH}/application.jar

EXPOSE ${APP_PORT}
ENTRYPOINT java -jar ${APPLICATION}