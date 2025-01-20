# <p align="center">Integration of Spring Boot And Apache Log4j2 With Relational Database Appender</p>

<p align="justify">

Log4j is a log framework and this tutorial shows how should be integrated Spring Boot 3 and Log4j2 to send logs to a
database. For more information see [https://logging.apache.org/log4j/2.x](https://logging.apache.org/log4j/2.x).

</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [Install MySQL on Docker](#install-mysql-on-docker)
* [How To Set up Spring Boot](#how-to-config-application)
* [Appendix](#appendix)

## Getting Started

### Prerequisites

* [Java 21](https://www.oracle.com/java/technologies/downloads)
* [Maven 3](https://maven.apache.org/index.html)
* [MySQL](https://www.mysql.com)
* [Docker](https://www.docker.com)

### Build

```shell
mvn clean package -DskipTests=true 
```

### Test

```shell
mvn test
```

### Run

```shell
mvn  spring-boot:run
```

### Pipeline

```shell
make LocalPipeline
```

```shell
make DockerizedPipeline
```

```shell
make e2e-test
```

```shell
docker compose --file ./docker-compose.yml --project-name dev-env down
```

## Dockerize

Create a docker compose file named `docker-compose.yml` then copy and paste the following script. There are two clients
to help you for interacting with MySQL therefore you can select one of
them, [MySQL workbench](https://www.mysql.com/products/workbench) or [Adminer](https://www.adminer.org/). If you install
MySQL via docker compose script mentioned below then database will be initialized and configured and, you don't need to
initialize it manually.

### Docker Compose

There are two options in order to connect to MySQL to management, MySQL Workbench, Adminer.

#### With MySQL Workbench

[docker-compose.yml](docker-compose.yml)

```yaml
#docker-compose.yml
version: "3.8"

services:
  mysql:
    image: mysql:8.0
    container_name: mysql
    hostname: mysql
    restart: always
    ports:
      - "3306:3306"
    environment:
      - MYSQL_USER=user
      - MYSQL_PASSWORD=password
      - MYSQL_DATABASE=tutorial_db
      - MYSQL_ROOT_PASSWORD=root
    volumes:
      - "./target/mysql:/etc/mysql/conf.d"
      - "./src/main/resources/init.sql:/docker-entrypoint-initdb.d/init.sql"
  mysql-workbench:
    image: lscr.io/linuxserver/mysql-workbench:latest
    container_name: mysql-workbench
    hostname: mysql-workbench
    restart: unless-stopped
    ports:
      - "3000:3000"
      - "3001:3001"
    environment:
      - PUID=1000
      - PGID=1000
      - TZ=Etc/UTC
    volumes:
      - "./target/mysql-workbench/config:/config"
    cap_add:
      - IPC_LOCK
```

#### With Adminer

[docker-compose.yml](docker-compose.yml)

```yaml
#docker-compose.yml
version: "3.8"

services:
  mysql:
    image: mysql:8.0
    container_name: mysql
    hostname: mysql
    restart: always
    ports:
      - "3306:3306"
    environment:
      - MYSQL_USER=user
      - MYSQL_PASSWORD=password
      - MYSQL_DATABASE=tutorial_db
      - MYSQL_ROOT_PASSWORD=root
    volumes:
      - "./target/mysql:/etc/mysql/conf.d"
      - "./src/main/resources/init.sql:/docker-entrypoint-initdb.d/init.sql"
  adminer:
    image: adminer
    container_name: adminer
    hostname: adminer
    restart: always
    ports:
      - "8080:8080"
```

### Deploy

Execute the following command to install MySQL.

```shell
docker compose --file ./docker-compose.yml --project-name dev-env up --build -d
```

### Down

Execute the following command to stop and remove MySQL.

```shell
docker compose --file ./docker-compose.yml --project-name dev-env down
```

### Init MySQL

After installation a MySQL database, create new user, database and create the `LOG_TABLE` table. This step apply
automatically in this tutorial.

Connect to MySQL with following command.

```shell
# try to connect to docker container
docker exec -it mysql mysql -h hostname -u user -p

# Example
#   user:     user
#   password: password 
#   hostname: localhost
docker exec -it mysql mysql -h localhost -u user -p
```

Execute following queries.

```mysql
# init.db
CREATE USER IF NOT EXISTS 'user'@'localhost' IDENTIFIED BY 'password';
CREATE DATABASE IF NOT EXISTS tutorial_db;
USE tutorial_db;

CREATE TABLE LOG_TABLE
(
    ID         INT PRIMARY KEY AUTO_INCREMENT,
    EVENT_DATE TIMESTAMP,
    LEVEL      VARCHAR(10),
    LOGGER     VARCHAR(255),
    MESSAGE    VARCHAR(4000)
);
```

### Web Console

#### MySQL Workbench

Open [http://localhost:3000](http://localhost:3000) in the browser to access MySQL Workbench dashboard.

```yaml
Hostname: mysql
Port: 3306
Username: user
Password: password
```

#### Adminer

Open [http://localhost:8080](http://localhost:8080) in the browser to access MySQL Workbench dashboard.

```yaml
Server: mysql:3306
Username: user
Password: password
```

## How To Set up Spring Boot

### Dependencies

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
        <exclusions>
            <exclusion>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-logging</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-log4j2</artifactId>
    </dependency>
    <!--append your database driver-->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
    </dependency>
</dependencies>
```

### Log4j Properties

Create a bundle named `log4j2_en.properties` include the following properties in the resources. In this case I am using
MySQL properties.

```properties
#log4j2_en.properties
driver=com.mysql.cj.jdbc.Driver
url=jdbc:mysql://localhost:3306/tutorial_db
table_name=LOG_TABLE
username=user
password=password
```

Create `log4j2.xml` in the resources with proper configuration for the database.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN">
    <Properties>
        <Property name="LOG_PATTERN">%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</Property>
        <Property name="LOG_LEVEL">INFO</Property>
    </Properties>
    <Appenders>
        <Console name="ConsoleAppender" target="SYSTEM_OUT">
            <PatternLayout pattern="${LOG_PATTERN}"/>
        </Console>
        <JDBC name="databaseAppender" tableName="${bundle:log4j2:table_name}" bufferSize="1" ignoreExceptions="false">
            <DriverManager
                    driverClassName="${bundle:log4j2:driver}"
                    connectionString="${bundle:log4j2:url}"
                    userName="${bundle:log4j2:username}"
                    password="${bundle:log4j2:password}"/>
            <Column name="EVENT_DATE" isEventTimestamp="true"/>
            <Column name="LEVEL" pattern="%level"/>
            <Column name="LOGGER" pattern="%logger"/>
            <Column name="MESSAGE" pattern="%message"/>
        </JDBC>
    </Appenders>
    <Loggers>
        <Root level="${LOG_LEVEL}">
            <AppenderRef ref="ConsoleAppender"/>
            <AppenderRef ref="databaseAppender"/>
        </Root>
        <Logger name="package-name" level="${LOG_LEVEL}" additivity="false">
            <AppenderRef ref="ConsoleAppender"/>
            <AppenderRef ref="databaseAppender"/>
        </Logger>
    </Loggers>
</Configuration>
```

## Appendix

### Makefile

```makefile
build:
	mvn clean package -DskipTests=true

test:
	mvn test

run:
	mvn spring-boot:run

DockerComposeDeploy:
	docker compose --file ./docker-compose.yml --project-name mysql up --build -d

docker-remove-container:
	docker rm mysql --force
	docker rm mysql-workbench- --force
	docker rm adminer --force

DockerRemoveImage:
	docker image rm mysql:8.0
	docker image rm lscr.io/linuxserver/mysql-workbench:latest
	docker image rm adminer

```

## Appendix

### Makefile

```makefile
build:
	mvn clean package -DskipTests=true

test:
	mvn test

run:
	mvn spring-boot:run
```

##

**<p align="center"> [Top](#integration-of-spring-boot-and-apache-log4j2-with-relational-database-appender) </p>**
