# <p align="center">Integration of Spring Boot And MySQL</p>

<p align="justify">

This tutorial is about integration of Spring Boot and [MySQL](https://www.mysql.com) and included configuration for test
and none test environments.

</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [MySQL](#mysql)
* [MySQL Use Cases](#mysql-use-cases)
* [Install MySQL on Docker](#install-mysql-on-docker)
* [Install MySQL on Kubernetes](#install-mysql-on-kubernetes)
* [How To Set up Spring Boot](#how-to-set-up-spring-boot)
* [How To Set up Spring Boot Test](#how-to-set-up-spring-boot-test)
* [Appendix](#appendix )

## Getting Started

### Prerequisites

* [Java 21](https://www.oracle.com/java/technologies/downloads/)
* [Maven 3](https://maven.apache.org/index.html)
* [MySQL](https://www.mysql.com)
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

## MySQL

### URL

The URL follows the syntax that mentioned below.

```yaml
url: jdbc:mysql://host:port/database-name
```

There are a few parameters included in the connection string of MySQL as follows.

* useUnicode=true
* useJDBCCompliantTimezoneShift=true
* useLegacyDatetimeCode=false
* serverTimezone=UTC
* pinGlobalTxToPhysicalConnection=TRUE

### Service Commands

After installing MySQL, it is possible to work with MySQL service based on command line.

Use `mysql -u root -p -h localhost` in order to connect to MySQL database in `localhost` also you need password of
user `root`.

```shell
# try to connect
mysql -u root -p -h hostname

# import database
mysql -u root -p < file.sql
# import data
mysql -u root -p db-name < file.sql

# backup database
mysqldump mysql -u root -p [–no-data] db-name [–no-create-info] > file.sql
mysqldump mysql -u root -p [–no-data] -databases db-name1 db-name2 ... [–no-create-info] > file.sql
mysqldump mysql -u root -p [–no-data] all-databases [–no-create-info] > file.sql
mysqldump mysql -u root -p [–no-data] db-name table-name1 table-name2 ... [–no-create-info] > file.sql
```

### SQL Commands

A few SQL commands to create schema (database), user and grant to the user.

```mysql
# schema (database)
SHOW DATABASES;
CREATE DATABASE IF NOT EXISTS 'db-name' DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
USE dbname;
DROP DATABASE dbname;
SHOW TABLES;

# user
CREATE USER IF NOT EXISTS 'user'@'hostname' IDENTIFIED BY 'password';

# gran
GRANT privilege ON dbname.tablename TO 'user'@'hostname';
FLUSH PRIVILEGES;

# example, assign gran to an user for executing XA transactions 
GRANT BINLOG_ADMIN, SYSTEM_VARIABLES_ADMIN ON *.* TO 'user'@'%';
GRANT XA_RECOVER_ADMIN ON *.* TO 'user'@'%';
GRANT ALL ON `test_db`.* TO 'user'@'%';
FLUSH PRIVILEGES;
```

<p align="justify">

For more information about MySQL see the [MySQL](https://www.mysql.com).

</p>

## MySQL Use Cases

* Web Applications
* E-commerce

## Install MySQL on Docker

Create a file named `docker-compose.yml` with the following configuration.

### Docker Compose

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
      - MYSQL_DATABASE=test_db
      - MYSQL_ROOT_PASSWORD=root
    volumes:
      - "./target/mysql:/etc/mysql/conf.d"
#      - "./src/main/resources/init.sql:/docker-entrypoint-initdb.d/init.sql"
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
      - MYSQL_DATABASE=test_db
      - MYSQL_ROOT_PASSWORD=root
    volumes:
      - "./target/mysql:/etc/mysql/conf.d"
#      - "./src/main/resources/init.sql:/docker-entrypoint-initdb.d/init.sql"
  adminer:
    image: adminer
    container_name: adminer
    hostname: adminer
    restart: always
    ports:
      - "8080:8080"
```

#### With PhpMyAdmin

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
      - MYSQL_DATABASE=test_db
      - MYSQL_ROOT_PASSWORD=root
    volumes:
      - "./target/mysql:/etc/mysql/conf.d"
#      - "./src/main/resources/init.sql:/docker-entrypoint-initdb.d/init.sql"
  phpmyadmin:
    image: phpmyadmin
    container_name: phpmyadmin
    hostname: phpmyadmin
    restart: always
    ports:
      - "8081:80"
    environment:
      - PMA_ARBITRARY=1
```

### Apply Docker Compose

Execute the following command to install MySQL.

```shell
docker compose --file ./docker-compose.yml --project-name mysql up --build -d

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

#### PhpMyAdmin

Open [http://localhost:8081](http://localhost:8081) in the browser to access MySQL Workbench dashboard.

```yaml
Server: mysql:3306
Username: user
Password: password
```

## Install MySQL on Kubernetes

Create the following files for installing MySQL.

### Kube Files

[mysql-secrets.yml](/kube/mysql-secrets.yml)

```yaml
# mysql-secrets.yml
apiVersion: v1
kind: Secret
metadata:
  name: mysql-secrets
type: Opaque
data:
  # value: root
  mysql-root-password: cm9vdA==
  # value: user
  mysql-user: dXNlcg==
  # value: password
  mysql-password: cGFzc3dvcmQ=
```

[mysql-configmap.yml](/kube/mysql-configmap.yml)

```yaml
# mysql-configmap.yml
apiVersion: v1
kind: ConfigMap
metadata:
  name: mysql-configmap
data:
  mysql-database: test_db
# if you want to add privilege the user and database to support XA transactions you have to add the following queries
#  initdb.sql: |-
#    CREATE DATABASE IF NOT EXISTS `test_db` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
#    CREATE USER IF NOT EXISTS 'user' IDENTIFIED BY 'password';
#    GRANT BINLOG_ADMIN, SYSTEM_VARIABLES_ADMIN ON *.* TO 'user';
#    GRANT XA_RECOVER_ADMIN ON *.* TO 'user';
#    GRANT ALL ON `test_db`.* TO 'user';
#    FLUSH PRIVILEGES;
```

[mysql-pvc.yml](/kube/mysql-pvc.yml)

```yaml
# mysql-pvc.yml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: mysql-pvc
  labels:
    app: mysql
    tier: database
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 1Gi
```

[mysql-deployment.yml](/kube/mysql-deployment.yml)

```yaml
# mysql-deployment.yml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: mysql
  labels:
    app: mysql
    tier: database
spec:
  selector:
    matchLabels:
      app: mysql
      tier: database
  strategy:
    type: Recreate
  template:
    metadata:
      labels:
        app: mysql
        tier: database
    spec:
      containers:
        - image: mysql:8.0
          name: mysql
          imagePullPolicy: "IfNotPresent"
          env:
            - name: MYSQL_ROOT_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: mysql-secrets
                  key: mysql-root-password
            - name: MYSQL_USER
              valueFrom:
                secretKeyRef:
                  name: mysql-secrets
                  key: mysql-user
            - name: MYSQL_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: mysql-secrets
                  key: mysql-password
            - name: MYSQL_DATABASE
              valueFrom:
                configMapKeyRef:
                  name: mysql-configmap
                  key: mysql-database
          ports:
            - name: mysql
              containerPort: 3306
          volumeMounts:
            - name: mysql-persistent-storage
              mountPath: /var/lib/mysql
      #            - name: mysql-initdb
      #              mountPath: /docker-entrypoint-initdb.d
      volumes:
        - name: mysql-persistent-storage
          persistentVolumeClaim:
            claimName: mysql-pvc
#        - name: mysql-initdb
#          configMap:
#            name: mysql-configmap
```

[mysql-service.yml](/kube/mysql-service.yml)

```yaml
# mysql-service.yml
apiVersion: v1
kind: Service
metadata:
  name: mysql
  labels:
    app: mysql
    tier: database
spec:
  selector:
    app: mysql
    tier: database
  ports:
    - port: 3306
      targetPort: 3306
```

[mysql-workbench-deployment.yml](/kube/mysql-workbench-deployment.yml)

```yaml
# mysql-workbench-deployment.yml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: mysql-workbench
  labels:
    app: mysql-workbench
spec:
  replicas: 1
  selector:
    matchLabels:
      app: mysql-workbench
  template:
    metadata:
      labels:
        app: mysql-workbench
    spec:
      containers:
        - name: mysql-workbench
          image: lscr.io/linuxserver/mysql-workbench:latest
          ports:
            - containerPort: 3000
            - containerPort: 3001
          env:
            - name: PUID
              value: "1000"
            - name: PGID
              value: "1000"
            - name: TZ
              value: "Etc/UTC"
          securityContext:
            capabilities:
              add: [ "IPC_LOCK" ]
```

[mysql-workbench-service.yml](/kube/mysql-workbench-service.yml)

```yaml
# mysql-workbench-service.yml
apiVersion: v1
kind: Service
metadata:
  name: mysql-workbench
spec:
  selector:
    app: mysql-workbench
  ports:
    - name: http-port
      port: 3000
      targetPort: 3000
    - name: https-port
      port: 3001
      targetPort: 3001
```

[adminer-deployment.yml](/kube/adminer-deployment.yml)

```yaml
# adminer-deployment.yml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: adminer
spec:
  replicas: 1
  selector:
    matchLabels:
      app: adminer
  template:
    metadata:
      labels:
        app: adminer
    spec:
      containers:
        - name: adminer
          image: adminer:latest
          ports:
            - containerPort: 8080
```

[adminer-service.yml](/kube/adminer-service.yml)

```yaml
# adminer-service.yml
apiVersion: v1
kind: Service
metadata:
  name: adminer
spec:
  selector:
    app: adminer
  ports:
    - protocol: TCP
      port: 8080
      targetPort: 8080
```

[phpmyadmin-deployment.yml](/kube/phpmyadmin-deployment.yml)

```yaml
# phpmyadmin-deployment.yml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: phpmyadmin
  labels:
    app: phpmyadmin
spec:
  replicas: 1
  selector:
    matchLabels:
      app: phpmyadmin
  strategy:
    type: Recreate
  template:
    metadata:
      labels:
        app: phpmyadmin
    spec:
      containers:
        - name: phpmyadmin
          image: phpmyadmin/phpmyadmin
          imagePullPolicy: "IfNotPresent"
          ports:
            - containerPort: 80
          env:
            - name: PMA_HOST
              value: mysql
            - name: PMA_PORT
              value: "3306"
            - name: MYSQL_ROOT_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: mysql-secrets
                  key: mysql-root-password
```

[phpmyadmin-service.yml](/kube/phpmyadmin-service.yml)

```yaml
# phpmyadmin-service.yml
apiVersion: v1
kind: Service
metadata:
  name: phpmyadmin
spec:
  selector:
    app: phpmyadmin
  ports:
    - protocol: TCP
      port: 80
      targetPort: 80
```

### Apply Kube Files

Execute the following commands to install the tools on Kubernetes.

```shell
kubectl apply -f ./kube/mysql-pvc.yml
kubectl apply -f ./kube/mysql-configmap.yml
kubectl apply -f ./kube/mysql-secrets.yml
kubectl apply -f ./kube/mysql-deployment.yml
kubectl apply -f ./kube/mysql-service.yml
kubectl apply -f ./kube/mysql-workbench-deployment.yml
kubectl apply -f ./kube/mysql-workbench-service.yml
kubectl apply -f ./kube/adminer-deployment.yml
kubectl apply -f ./kube/adminer-service.yml
kubectl apply -f ./kube/phpmyadmin-deployment.yml
kubectl apply -f ./kube/phpmyadmin-service.yml
```

### Check Status

```shell
kubectl get all
```

### Port Forwarding

<p align="justify">

In order to connect to MySQL from localhost through the application use the following command and MySQL is available
on `localhost:3306`.

</p>

```shell
kubectl port-forward service/mysql port:port
```

<p align="justify">

In order to connect to MySQL-Workbench from localhost through the web browser use the following command and dashboard of
MySQL-Workbench is available on [http://localhost:3000](http://localhost:3000) URL.

</p>

```shell
kubectl port-forward service/mysql-workbench 3306:3306
```

<p align="justify">

In order to connect to Adminer from localhost through the web browser use the following command and dashboard of
Adminer is available on [http://localhost:8080](http://localhost:8080) URL.

</p>

```shell
kubectl port-forward service/adminer 8080:8080
```

<p align="justify">

In order to connect to PhpMyAdmin from localhost through the web browser use the following command and dashboard of
PhpMyAdmin is available on [http://localhost:8081](http://localhost:8081) URL.

</p>

```shell
kubectl port-forward service/phpmyadmin 8081:80
```

## How To Set up Spring Boot

### Dependencies

```xml

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <scope>runtime</scope>
    </dependency>
</dependencies>
```

### Application Properties

```yaml
spring:
  datasource:
    username: ${DATABASE_USERNAME:user}
    password: ${DATABASE_PASSWORD:password}
    url: jdbc:mysql://${MYSQL_HOST:localhost}:${MYSQL_PORT:3306}/${DATABASE_NAME:test_db}
    driver-class-name: com.mysql.cj.jdbc.Driver
  data:
    jpa:
      repositories:
        enabled: true
  jpa:
    database: MYSQL
    database-platform: org.hibernate.dialect.MySQL8Dialect
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
        default_schema: ${DATABASE_SCHEMA:sample}
```

## How To Set up Spring Boot Test

### Dependencies

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
            <artifactId>mysql</artifactId>
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

```shell
build:
	mvn clean package -DskipTests=true

test:
	mvn test

run:
	mvn spring-boot:run

docker-compose-deploy:
	docker compose --file docker-compose.yml --project-name mysql up --build -d

docker-remove-container:
	docker rm mysql --force
	docker rm mysql-workbench- --force
	docker rm adminer --force
	docker rm phpmyadmin --force

docker-remove-image:
	docker image rm mysql:8.0
	docker image rm lscr.io/linuxserver/mysql-workbench:latest
	docker image rm adminer
	docker image rm phpmyadmin

kube-deploy:
	kubectl apply -f ./kube/mysql-pvc.yml
	kubectl apply -f ./kube/mysql-configmap.yml
	kubectl apply -f ./kube/mysql-secrets.yml
	kubectl apply -f ./kube/mysql-deployment.yml
	kubectl apply -f ./kube/mysql-service.yml
	kubectl apply -f ./kube/mysql-workbench-deployment.yml
	kubectl apply -f ./kube/mysql-workbench-service.yml
	kubectl apply -f ./kube/adminer-deployment.yml
	kubectl apply -f ./kube/adminer-service.yml
	kubectl apply -f ./kube/phpmyadmin-deployment.yml
	kubectl apply -f ./kube/phpmyadmin-service.yml

kube-delete:
	kubectl delete all --all
	kubectl delete secrets mysql-secrets
	kubectl delete configMap mysql-configmap
	kubectl delete persistentvolumeclaim mysql-pvc

kube-port-forward-mysql:
	kubectl port-forward service/mysql 3306:3306

kube-port-forward-adminer:
	kubectl port-forward service/adminer 8080:8080

kube-port-forward-phpmyadmin:
	kubectl port-forward service/phpmyadmin 8081:80

kube-port-forward-mysql-workbench:
	kubectl port-forward service/mysql-workbench 3000:3000
```

##

**<p align="center">[Top](#integration-of-spring-boot-and-mysql)</p>**