# <p align="center">Integration of Spring Boot And Oracle</p>

<p align="justify">

This tutorial is about integration of Spring Boot and Oracle and is
included [Oracle](https://www.oracle.com) configuration for test and none test environment.

</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [Oracle](#oracle)
* [Install Oracle on Docker](#install-oracle-on-docker)
* [Install Oracle on Kubernetes](#install-oracle-on-kubernetes)
* [How To Set up Spring Boot](#how-to-set-up-spring-boot)
* [How To Set up Spring Boot Test](#how-to-set-up-spring-boot-test)
* [Appendix](#appendix )

## Getting Started

### Prerequisites

* [Java 21](https://www.oracle.com/java/technologies/downloads/)
* [Maven 3](https://maven.apache.org/index.html)
* [Oracle](https://www.oracle.com)
* [Docker](https://www.docker.com/)
* [Kubernetes](https://kubernetes.io/)

### Pipeline

#### Build

```bash
mvn clean package -DskipTests=true 
```

#### Test

```bash
mvn test
```

#### Run

```bash
mvn  spring-boot:run
```

## Oracle

<p align="justify">

For more information about Oracle see the [Oracle](https://www.oracle.com).

</p>

### URL

```yaml
url: jdbc:oracle:thin:${ORACLE_HOST:localhost}:${ORACLE_PORT:1521}/${DATABASE_NAME:xepdb1}
```

### Sqlplus Command

```oraclesqlplus
-- execute sql file
@/tmp/ords_installer_privileges.sql testuser;
```

### SQL Command

```oracle
CREATE USER testuser IDENTIFIED BY password;
GRANT DBA TO testuser;
```

## Install Oracle on Docker

Create a file named `docker-compose.yml` with the following configuration.

### Docker Compose

[docker-compose.yml](docker-compose.yml)

```yaml
#docker-compose.yml
version: "3.8"

services:
  oracle:
    image: container-registry.oracle.com/database/express:21.3.0-xe
    container_name: oracle
    hostname: oracle
    restart: always
    ports:
      - "1521:1521"
      - "5500:5500"
    environment:
      ORACLE_PWD: password
    volumes:
      - "oracle_data:/opt/oracle/oradata"
  ords:
    image: container-registry.oracle.com/database/ords:latest
    container_name: ords
    hostname: ords
    depends_on:
      - oracle
    ports:
      - "8080:8080"
    volumes:
      - "ords_config:/etc/ords/config"
    entrypoint: [ "ords", "serve" ]

volumes:
  ords_config:
    driver: local
  oracle_data:
    driver: local
```

### Apply Docker Compose

Execute the following command to install Oracle.

```shell
docker compose --file ./docker-compose.yml --project-name oracle up --build -d
```

### Enterprise Manager

Open https://localhost:5500/em in web browser.

* user: system
* password: password
* container name: xepdb1

### Sqlplus

```shell
sqlplus username/password@//hostname:port/servicename

# Example
sqlplus sys/password@//localhost:1521/xepdb1
```

### ORDS (Sql Developer Web)

Open [http://localhost:8080/ords](http://localhost:8080/ords/sql-developer) but, you cannot log in to SQL Developer
before applying the following configuration.

Get `ords_installer_privileges.sql` file and then connect to the database.

```shell
# download from ords container
docker cp ords:/opt/oracle/ords/scripts/installer/ords_installer_privileges.sql ./
# upload to oracle container
docker cp ./ords_installer_privileges.sql oracle:/tmp
# connect to database
docker exec -it oracle sqlplus sys/password@//localhost:1521/xepdb1 as sysdba
```

You have to enable the ORDS schema for the target user.

```oraclesqlplus
CREATE USER testuser IDENTIFIED BY password;
GRANT DBA TO testuser;
@/tmp/ords_installer_privileges.sql testuser;
```

Apply configuration to ORDS instance.

```shell
docker exec -it ords ords --config /opt/ords/config install
# hostname: oracle
# port: 1521
# service name: xepdb1
# username: testuser
# password: password

docker restart ords
```

Connect to Oracle database and execute the procedure.

```shell
docker exec -it oracle sqlplus testuser/password@//localhost:1521/xepdb1

```

```oracle-plsql
 begin
    ords.enable_schema(
        p_enabled => true,
        p_schema => 'testuser',
        p_url_mapping_type => 'BASE_PATH',
        p_url_mapping_pattern => 'testuser',
        p_auto_rest_auth => false
    );
    commit;
end;
/
```

Open [http://localhost:8080/ords/sql-developer](http://localhost:8080/ords/sql-developer) in web browser, then use
following credentials to log in to the `Sql Developer Web`.

```yaml
Username: testuser
Password: password
```

## Install Oracle on Kubernetes

Create the following files for installing Oracle.

### Kube Files

[oracle-pvc.yml](/kube/oracle-pvc.yml)

```yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: oracle-pvc
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 1Gi
```

[oracle-deployment.yml](/kube/oracle-deployment.yml)

```yaml
#oracle-deployment.yml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: oracle
spec:
  replicas: 1
  selector:
    matchLabels:
      app: oracle
  template:
    metadata:
      labels:
        app: oracle
    spec:
      containers:
        - name: oracle
          image: container-registry.oracle.com/database/express:21.3.0-xe
          ports:
            - containerPort: 1521
            - containerPort: 5500
          env:
            - name: ORACLE_PWD
              value: password
          volumeMounts:
            - mountPath: /opt/oracle/oradata
              name: oracle-data
      volumes:
        - name: oracle-data
          persistentVolumeClaim:
            claimName: ords-config-pvc
```

[oracle-service.yml](/kube/oracle-service.yml)

```yaml
#oracle-service.yml
apiVersion: v1
kind: Service
metadata:
  name: oracle
spec:
  selector:
    app: oracle
  ports:
    - name: tcp
      port: 1521
      targetPort: 1521
    - name: em
      port: 5500
      targetPort: 5500
```

[ords-pvc.yml](/kube/ords-pvc.yml)

```yaml
#ords-pvc.yml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: ords-config-pvc
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 1Gi

```

[ords-deployment.yml](/kube/ords-deployment.yml)

```yaml
#ords-deployment.yml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: ords
spec:
  replicas: 1
  selector:
    matchLabels:
      app: ords
  template:
    metadata:
      labels:
        app: ords
    spec:
      containers:
        - name: ords
          image: container-registry.oracle.com/database/ords:latest
          ports:
            - containerPort: 8080
          volumeMounts:
            - mountPath: /etc/ords/config
              name: ords-config
          command: [ "ords", "serve" ]
      volumes:
        - name: ords-config
          persistentVolumeClaim:
            claimName: ords-config-pvc
```

[ords-service.yml](/kube/ords-service.yml)

```yaml
#ords-service.yml
apiVersion: v1
kind: Service
metadata:
  name: ords
spec:
  selector:
    app: ords
  ports:
    - protocol: TCP
      port: 8080
      targetPort: 8080
```

### Apply Kube Files

Execute the following commands to install the tools on Kubernetes.

```shell
kubectl apply -f ./kube/oracle-pvc.yml
kubectl apply -f ./kube/oracle-deployment.yml
kubectl apply -f ./kube/oracle-service.yml
kubectl apply -f ./kube/ords-pvc.yml
kubectl apply -f ./kube/ords-deployment.yml
kubectl apply -f ./kube/ords-service.yml
```

### Check Status

```shell
kubectl get all
```

### Port Forwarding

<p align="justify">

In order to connect to Oracle from localhost through the application use the following command and Oracle is available
on `localhost:1521`.

</p>

```shell
kubectl port-forward service/oracle 1521:1521
```

<p align="justify">

In order to connect to Ords from localhost through the web browser use the following command and dashboard of Ords is
available on [http://localhost:8080/ords](http://localhost:8080/ords) URL.

</p>

```shell
kubectl port-forward service/ords 8080:8080
```

## How To Set up Spring Boot

### Dependencies

The Oracle database driver should
be [downloaded](https://www.oracle.com/database/technologies/appdev/jdbc-downloads.html) and install manually with
following command.

#### Linux/Unix

```shell
#!/usr/bin/env bash

mvn install:install-file \
-Dfile=path/ojdbc11.jar \
-DgroupId=com.oracle \
-DartifactId=ojdbc11 \
-Dversion=21c \
-Dpackaging=jar
```

#### Windows

```shell
mvn install:install-file ^
-Dfile=./ojdbc11.jar ^
-DgroupId=com.oracle ^
-DartifactId=ojdbc11 ^
-Dversion=21c ^
-Dpackaging=jar ^
-DgeneratePom=true

```

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>com.oracle</groupId>
        <artifactId>ojdbc11</artifactId>
        <version>21c</version>
    </dependency>
</dependencies>
```

### Application Properties

```yaml
spring:
  datasource:
    username: ${DATABASE_USERNAME:system}
    password: ${DATABASE_PASSWORD:password}
    url: jdbc:oracle:thin:${ORACLE_HOST:localhost}:${ORACLE_PORT:1521}/${DATABASE_NAME:xepdb1}
    driver-class-name: oracle.jdbc.driver.OracleDriver
  data:
    jpa:
      repositories:
        enabled: true
  jpa:
    database: ORACLE
    database-platform: org.hibernate.dialect.OracleDialect
    defer-datasource-initialization: true
    show-sql: true
    hibernate:
      ddl-auto: update
    properties:
      javax:
        persistence:
          create-database-schemas: true
      hibernate:
        generate_statistics: true
        format_sql: true
        naming-strategy: org.hibernate.cfg.ImprovedNamingStrategy
        default_schema: ${spring.datasource.username}

```

## How To Set up Spring Boot Test

### Dependency

```xml

<project>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.testcontainers</groupId>
                <artifactId>testcontainers-bom</artifactId>
                <version>1.18.3</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>junit-jupiter</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.testcontainers</groupId>
            <artifactId>oracle-xe</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
```

### Application Properties

```yaml
---
spring:
  config:
    activate:
      on-profile: test
  jpa:
    hibernate:
      ddl-auto: create
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

docker-compose-deploy:
	docker compose --file docker-compose.yml --project-name oracle up --build -d

docker-remove-container:
	docker rm oracle --force
	docker rm ords --force

docker-remove-image:
	docker image rm container-registry.oracle.com/database/express:21.3.0-xe
	docker image rm container-registry.oracle.com/database/ords:latest

kube-deploy:
	kubectl apply -f ./kube/oracle-pvc.yml
	kubectl apply -f ./kube/oracle-deployment.yml
	kubectl apply -f ./kube/oracle-service.yml
	kubectl apply -f ./kube/ords-pvc.yml
	kubectl apply -f ./kube/ords-deployment.yml
	kubectl apply -f ./kube/ords-service.yml

kube-delete:
	kubectl delete all --all

kube-port-forward-oracle:
	kubectl port-forward service/oracle 1521:1521

kube-port-forward-ords:
	kubectl port-forward service/ords 8080:8080
```

##

**<p align="center"> [Top](#integration-of-spring-boot-and-oracle) </p>**