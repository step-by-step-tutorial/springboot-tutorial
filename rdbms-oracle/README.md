# <p style="text-align: center;"> Integration of Spring Boot And Oracle</p>

<p style="text-align: justify;">
This tutorial demonstrates the integration of Spring Boot and [Oracle](https://www.oracle.com), including configuration for test and non-test environments.
</p>

## <p style="text-align: center;"> Table of Content </p>

* [Getting Started](#getting-started)
* [Dockerized](#dockerized)
* [Kubernetes](#kubernetes)
* [UI](#ui)
* [Sqlplus](#sqlplus)
* [ORDS](#ords-sql-developer-web)
* [Oracle](#oracle)

## Getting Started

### Prerequisites

* [Java 21](https://www.oracle.com/java/technologies/downloads)
* [Maven 3](https://maven.apache.org/index.html)
* [Docker](https://www.docker.com)
* [Kubernetes](https://kubernetes.io)

### Build

```shell
mvn validate clean compile 
```

### Test

```shell
mvn test
```

### Package

```shell
mvn package -DskipTests=true
```

### Run

```shell
mvn spring-boot:start
```

### E2eTest

```shell
curl -X GET http://localhost:8080/api/v1/health-check
```

### Stop

```shell
mvn spring-boot:stop
```

### Verify

```shell
mvn verify -DskipTests=true
docker volume prune -f
```

## Dockerized

### Deploy

```shell
mvn clean package verify -DskipTests=true
docker compose --file docker-compose.yml --project-name dev up --build -d
```

### E2eTest

```shell
curl -X GET http://localhost:8080/api/v1/health-check
```

### Down

```shell
docker compose --file docker-compose.yml --project-name dev down
docker image rm samanalishiri/application:latest
docker volume prune -f
```

## Kubernetes

### Deploy

```shell
mvn clean package verify -DskipTests=true
docker build -t samanalishiri/application:latest . --no-cache
kubectl apply -f kube-dev.yml
```

### Check Status

```shell
kubectl get all -n dev
```

### Port Forwarding

```shell
# Oracle Database
kubectl port-forward service/oracle 1521:1521 -n dev
```

```shell
# Oracle enterprise manager
kubectl port-forward service/oracle 5500:5500 -n dev
```

```shell
# ORDS (SQL Developer Web)
kubectl port-forward service/ords 8081:8080 -n dev
```

```shell
# Application
kubectl port-forward service/application 8080:8080 -n dev
```

### E2eTest

```shell
curl -X GET http://localhost:8080/api/v1/health-check
```

### Down

```shell
kubectl delete all --all -n dev
kubectl delete secrets dev-credentials -n dev
kubectl delete configMap dev-config -n dev
kubectl delete persistentvolumeclaim oracle-pvc -n dev
kubectl delete persistentvolumeclaim ords-config-pvc -n dev
docker image rm samanalishiri/application:latest
docker volume prune -f
```

## UI

* Oracle Enterprise Manager: [https://localhost:5500/em](https://localhost:5500/em)

```yaml
user: system
password: password
container name: xepdb1
```

* ORDS (SQL Developer Web): [http://localhost:8081/ords](http://localhost:8081/ords)
  Configure ORDS before login.

```yaml
Username: testuser
Password: password
```

---

## Sqlplus

```shell
sqlplus username/password@//hostname:port/servicename

# Example
sqlplus sys/password@//localhost:1521/xepdb1
```

## ORDS (Sql Developer Web)

Get `ords_installer_privileges.sql` file and then connect to the database.

```shell
# download from ords container
docker cp ords:/opt/oracle/ords/scripts/installer/ords_installer_privileges.sql ./
```

```shell
# upload to oracle container
docker cp ./ords_installer_privileges.sql oracle:/tmp
```

```shell
# connect to database
docker exec -it oracle sqlplus sys/password@//localhost:1521/xepdb1 as sysdba
```

You have to enable the ORDS schema for the target user. **Run the following commands one by one**.

```oraclesqlplus
CREATE USER testuser IDENTIFIED BY password;
GRANT DBA TO testuser;
@/tmp/ords_installer_privileges.sql testuser;
```

Apply configuration to the ORDS instance.

```shell
docker exec -it ords ords --config /etc/ords/config install
# [1] Basic (host name, port, service name)
# hostname: oracle
# port: 1521
# service name: xepdb1
# username: testuser
# password: password
# [A] Accept and Continue - Create configuration and Install ORDS in the database
```

```shell
docker restart ords
```

```shell
docker exec -it oracle sqlplus testuser/password@//localhost:1521/xepdb1

```

```oraclesqlplus
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

## Oracle

### URL Format

```yaml
url: jdbc:oracle:thin:${ORACLE_HOST:localhost}:${ORACLE_PORT:1521}/${DATABASE_NAME:xepdb1}
```

##

**<p style="text-align: center;"> [Top](#integration-of-spring-boot-and-oracle) </p>**