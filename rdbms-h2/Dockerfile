FROM eclipse-temurin:21-jdk-alpine

ARG JAR_PATH=./target
ARG JAR_NAME=rdbms-h2
ARG JAR_VERSION=0.0.1-SNAPSHOT
ARG TARGET_PATH=/app

RUN apk --no-cache add ca-certificates wget curl

ENV APPLICATION=${TARGET_PATH}/application.jar
ENV APP_HOST=0.0.0.0
ENV APP_PORT=8080
ENV APP_PROFILES=h2
ENV APP_DATABASE_NAME=tutorial_db

ADD ${JAR_PATH}/${JAR_NAME}-${JAR_VERSION}.jar ${TARGET_PATH}/application.jar

EXPOSE ${APP_PORT}
ENTRYPOINT java -jar ${APPLICATION}