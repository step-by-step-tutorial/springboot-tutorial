# <p align="center">RDBMS MySQL</p>

<p align="justify">

This tutorial is included [MySQL database](https://www.mysql.com/) configuration for test and none test environments.

</p>

### URL

The URL follows the syntax that mentioned below.

```text
jdbc:mysql://host:port/database-name
```

There are a few parameters included in the connection string of MySQL as follows.

* useUnicode=true
* useJDBCCompliantTimezoneShift=true
* useLegacyDatetimeCode=false
* serverTimezone=UTC
* pinGlobalTxToPhysicalConnection=TRUE

### How To

After installing MySQL, it is possible to work with MySQL service based on command line.

#### MySQL Service.

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

#### SQL Commands

A few SQL commands to create schema (database), user and grant to the user.

```iso92-sql
# schema (database)
SHOW DATABASES;
CREATE DATABASE IF NOT EXISTS 'db-name' DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
USE db-name;
DROP DATABASE db-name;
SHOW TABLES;

# user
CREATE USER IF NOT EXISTS 'user'@'localhost' IDENTIFIED BY 'password';

# gran
GRANT privilege-type ON db-name.table-name TO 'user'@'host';
FLUSH PRIVILEGES;


# example, assign gran to an user for executing XA transactions 
GRANT BINLOG_ADMIN, SYSTEM_VARIABLES_ADMIN ON *.* TO 'user'@'%';
GRANT XA_RECOVER_ADMIN ON *.* TO 'user'@'%';
GRANT ALL ON `test_db`.* TO 'user'@'%';
FLUSH PRIVILEGES;

# exit
EXIT;
```

## Install MySQL on Docker

### Docker Compose File

Create a file named docker-compose.yml with the following configuration.

```yaml
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
  adminer:
    image: adminer
    container_name: adminer
    hostname: adminer
    restart: always
    ports:
      - "8080:8080"
```

Execute the `docker compose  up -d` command to install MySQL and Adminer.

```shell
# full command
docker compose --file ./docker-compose.yml --project-name mysql up --build -d

```

### Adminer

<p align="justify">

In order to connect to MySQL by Adminer, open [http://localhost:8080](http://localhost:8080/) in the web browser and use
the following properties in the login page.

</p>

```yaml
system: MySQL
server: mysql:3306 #host:port
username: user
password: password
database: test_db
```

### PhpMyAdmin

There is another alternative for Adminer named phpMyAdmin.

```yaml
  phpmyadmin:
    image: phpmyadmin
    container_name: phpmyadmin
    hostname: phpmyadmin
    restart: always
    ports:
      - "8080:80"
    environment:
      - PMA_ARBITRARY=1
```

In order to connect to MySQL by phpMyAdmin open [http://localhost:8080](http://localhost:8080/) in the web browser.

## Install MySQL on Kubernetes

### MySQL

Create the following files for installing MySQL.

**mysql-secrets.yml**

```yaml
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

**mysql-configmap.yml**

```yaml
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

**mysql-pvc.yml**

```yaml
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

**mysql-deployment.yml**

```yaml
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

**mysql-service.yml**

```yaml
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

### Adminer

Create the following files for installing Adminer.

**adminer-deployment.yml**

```yaml
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

**adminer-service.yml**

```yaml
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

### Apply Configuration Files

Execute the following commands to install the tools on Kubernetes.

```shell
# ======================================================================================================================
# MySQL
# ======================================================================================================================
kubectl apply -f ./kube/mysql-pvc.yml
# kubectl get pvc 
# kubectl describe pvc mysql-pvc

kubectl apply -f ./kube/mysql-configmap.yml
# kubectl describe configmap mysql-configmap -n default

kubectl apply -f ./kube/mysql-secrets.yml
# kubectl describe secret mysql-secrets -n default
# kubectl get secret mysql-secrets -n default -o yaml

kubectl apply -f ./kube/mysql-deployment.yml
# kubectl get deployments -n default
# kubectl describe deployment mysql -n default

kubectl apply -f ./kube/mysql-service.yml
# kubectl get service -n default
# kubectl describe service mysql -n default

# ======================================================================================================================
# Adminer
# ======================================================================================================================
kubectl apply -f ./kube/adminer-deployment.yml
# kubectl get deployments -n default
# kubectl describe deployment adminer -n default

kubectl apply -f ./kube/adminer-service.yml
# kubectl get services -n default
# kubectl describe service adminer -n default

# ======================================================================================================================
# After Install
# ======================================================================================================================
kubectl get all
```

For connecting to MySQL through application on localhost it should be executed the following command.

```shell
# mysql
kubectl port-forward service/mysql 3306:3306
```

<p align="justify">

In order to connect to Adminer from localhost through the web browser use the following command and dashboard of Adminer
is available on [http://localhost:8080](http://localhost:8080) URL.

</p>

```shell
# adminer
# http://localhost:8080
kubectl port-forward service/adminer 8080:8080
```

Use the following properties for Adminer.

```yaml
system: MySQL
server: mysql:3306 #host:port
username: user
password: password
database: test_db
```

### PhpMyAdmin

Also, there is another alternative for Adminer named phpMyAdmin. Therefore, use the following instruction to install
that on Kubernetes.

**phpmyadmin-deployment.yml**

```yaml
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

**phpmyadmin-service.yml**

```yaml
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

Execute the following commands to install the tools on Kubernetes.

```shell
# ======================================================================================================================
# PhpMyAdmin
# ======================================================================================================================
kubectl apply -f ./kube/phpmyadmin-deployment.yml
# kubectl get deployments -n default
# kubectl describe deployment phpmyadmin -n default

kubectl apply -f ./kube/phpmyadmin-service.yml
# kubectl get services -n default
# kubectl describe service phpmyadmin -n default

kubectl get all
```

<p align="justify">

In order to connect to phpMyAdmin through the web browser on localhost use the following command and dashboard of
phpMyAdmin is available on [http://localhost:8080](http://localhost:8080) URL.

</p>

```shell
# phpmyadmin
# http://localhost:8080
kubectl port-forward service/phpmyadmin 8080:80
```

## How To Config Spring Boot

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

### Test Dependency

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

### Spring Boot Properties

```yaml
spring:
  datasource:
    username: ${DATABASE_NAME:user}
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
---
spring:
  config:
    activate:
      on-profile: test
  jpa:
    hibernate:
      ddl-auto: create

```

## Prerequisites

* [Java 21](https://www.oracle.com/java/technologies/downloads/)
* [Maven 3](https://maven.apache.org/index.html)
* [Docker](https://www.docker.com/)
* [Kubernetes](https://kubernetes.io/) (optional)

## Build

```bash
mvn clean package -DskipTests=true
```

## Test

```bash
mvn  test
```

## Run

```bash
mvn  spring-boot:run
```

##

**<p align="center">[Top](#rdbms-mysql)</p>**