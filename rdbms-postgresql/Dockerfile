FROM eclipse-temurin:21-jdk-alpine

ARG JAR_PATH=./target
ARG JAR_NAME=rdbms-postgresql
ARG JAR_VERSION=0.0.1-SNAPSHOT
ARG TARGET_PATH=/app

RUN apk --no-cache add ca-certificates wget curl

ENV APPLICATION=${TARGET_PATH}/application.jar
ENV APP_HOST=0.0.0.0
ENV APP_PORT=8080
ENV APP_PROFILES=postgresql
ENV POSTGRES_HOST=localhost
ENV POSTGRES_PORT=5432
ENV POSTGRES_DB=test_db
ENV POSTGRES_USER=user
ENV POSTGRES_PASSWORD=password

ADD ${JAR_PATH}/${JAR_NAME}-${JAR_VERSION}.jar ${TARGET_PATH}/application.jar

EXPOSE ${APP_PORT}
ENTRYPOINT java -jar ${APPLICATION} 