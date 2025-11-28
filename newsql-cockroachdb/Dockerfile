FROM eclipse-temurin:21-jdk-alpine

ARG JAR_PATH=./target
ARG JAR_NAME=newsql-cockroachdb
ARG JAR_VERSION=0.0.1-SNAPSHOT
ARG TARGET_PATH=/app

RUN apk --no-cache add ca-certificates wget curl

ENV APPLICATION=${TARGET_PATH}/application.jar
ENV APP_HOST=0.0.0.0
ENV APP_PORT=8080
ENV APP_PROFILES=cockroachdb
ENV DATABASE_HOST=localhost
ENV DATABASE_PORT=26257
ENV DATABASE_DB=defaultdb
ENV DATABASE_USER=root
ENV DATABASE_PASSWORD=password

ADD ${JAR_PATH}/${JAR_NAME}-${JAR_VERSION}.jar ${TARGET_PATH}/application.jar

EXPOSE ${APP_PORT}
ENTRYPOINT java -jar ${APPLICATION} 