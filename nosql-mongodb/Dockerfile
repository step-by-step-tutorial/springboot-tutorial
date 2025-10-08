FROM eclipse-temurin:21-jdk-alpine

ARG JAR_PATH=./target
ARG JAR_NAME=nosql-mongodb
ARG JAR_VERSION=0.0.1-SNAPSHOT
ARG TARGET_PATH=/app

RUN apk --no-cache add ca-certificates wget curl

ENV APPLICATION=${TARGET_PATH}/application.jar
ENV APP_HOST=0.0.0.0
ENV APP_PORT=8080
ENV APP_PROFILES=mongo
ENV MONGO_DB_USER=root
ENV MONGO_DB_PASS=root
ENV MONGO_DB_HOST=localhost
ENV MONGO_DB_PORT=27017
ENV MONGO_DB_NAME=tutorial_db

ADD ${JAR_PATH}/${JAR_NAME}-${JAR_VERSION}.jar ${TARGET_PATH}/application.jar

EXPOSE ${APP_PORT}
ENTRYPOINT java -jar ${APPLICATION}