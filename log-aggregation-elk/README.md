# <p align="center">Title</p>

<p align="justify">

This tutorial is about integration of Spring Boot and TOOLS_NAME.

</p>

## <p align="center"> Table of Content </p>

* [Getting Started](#getting-started)
* [TOOLS_NAME](#tools_name)
* [TOOLS_NAME Use Cases](#tools_name-use-cases)
* [Install TOOLS_NAME on Docker](#install-tools_name-on-docker)
* [Install TOOLS_NAME on Kubernetes](#install-tools_name-on-kubernetes)
* [How To Set up Spring Boot](#how-to-set-up-spring-boot)
* [How To Set up Spring Boot Test](#how-to-set-up-spring-boot-test)
* [Appendix](#appendix )

## Getting Started

### Prerequisites

* [Java 21](https://www.oracle.com/java/technologies/downloads/)
* [Maven 3](https://maven.apache.org/index.html)
* [Docker](https://www.docker.com/)
* [Kubernetes](https://kubernetes.io/)

### Pipeline

#### Build

```shell
mvn clean package -DskipTests=true 
```

#### Test

```shell
mvn test
```

#### Run

```shell
mvn  spring-boot:run
```

## TOOLS_NAME

<p align="justify">

For more information about TOOLS_NAME see the []().

</p>

## TOOLS_NAME Use Cases

## Install TOOLS_NAME on Docker

Create a file named `docker-compose.yml` with the following configuration.

### Docker Compose

[docker-compose.yml](docker-compose.yml)

```yaml
#docker-compose.yml
```

### Apply Docker Compose

Execute the following command to install TOOLS_NAME.

```shell
docker compose --file ./docker-compose.yml --project-name elk up --build -d
```

### Elasticsearch Check Status

```shell
curl -u elastic:password http://localhost:9200
```

Open [http://localhost:9200](http://localhost:9200) in browser then put the following information.

```yaml
Username: elastic
Password: password
```

### Elasticsearch User

#### kibana_system

```shell
# configure the Kibana password in the elasticsearch container
docker exec -it elasticsearch ./bin/elasticsearch-reset-password -u kibana_system -i
```

```shell
# configure the Kibana password via rest API
curl -u elastic:password -X POST -H "Content-Type: application/json" http://localhost:9200/_security/user/kibana_system/_password --data "{\"password\" : \"password\"}"
```

```shell
curl -u kibana_system:password http://localhost:9200/_security/_authenticate
```

#### logstash_system

```shell
# configure the Kibana password in the elasticsearch container
docker exec -it elasticsearch ./bin/elasticsearch-reset-password -u logstash_system -i
```

```shell
# configure the Kibana password via rest API
curl -u elastic:password -X POST -H "Content-Type: application/json" http://localhost:9200/_security/user/logstash_system/_password --data "{\"password\" : \"password\"}"
```

```shell
curl -u logstash_system:password http://localhost:9200/_security/_authenticate
```

#### New User

```shell
curl -X POST -u elastic:password  -H "Content-Type: application/json" localhost:9200/_security/user/kibana_user --data "{\"password\" : \"password\",\"roles\" : [ \"kibana_admin\", \"kibana_system\" ],\"full_name\" : \"Kibana User\"}"
```

### Kibana

Open [http://localhost:5601](http://localhost:5601) in browser then put the following information.

```yaml
Username: elastic
Password: password
```

```shell
docker exec kibana curl -u elastic:password http://elasticsearch:9200
```
```kibana
# to see indices
GET /_cat/indices?v
```

```kibana
# to see application logs
GET /logstash-2025.01.16/_search
{
  "query": {
    "match_all": {}
  },
  "from": 33, 
  "size": 50
}

```

### Logstash

Open [http://localhost:9600](http://localhost:5601) in browser.

```shell
docker exec logstash curl -u elastic:password http://elasticsearch:9200
```

## Install TOOLS_NAME on Kubernetes

Create the following files for installing TOOLS_NAME.

### Kube Files

[tools_name-deployment.yml](/kube/tools_name-deployment.yml)

```yaml
#deployment.yml
```

[tools_name-service.yml](/kube/tools_name-service.yml)

```yaml
#service.yml
```

### Apply Kube Files

Execute the following commands to install the tools on Kubernetes.

```shell
kubectl apply -f ./kube/tools_name-deployment.yml
kubectl apply -f ./kube/tools_name-service.yml
```

### Check Status

```shell
kubectl get all
```

### Port Forwarding

<p align="justify">

In order to connect to TOOLS_NAME from localhost through the web browser use the following command and dashboard of
TOOLS_NAME is available on [http://localhost:port](http://localhost:port) URL.

</p>

```shell
kubectl port-forward service/tools_name port:port
```

## How To Set up Spring Boot

### Dependencies

```xml
```

### Application Properties

```yaml
```

## How To Set up Spring Boot Test

### Dependencies

```xml
```

### Application Properties

```yaml
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
	docker compose --file docker-compose.yml --project-name tools-name up --build -d

docker-remove-container:
	docker rm tools-name --force

docker-remove-image:
	docker image rm image-name

kube-deploy:
	kubectl apply -f ./kube/tools-name-deployment.yml
	kubectl apply -f ./kube/tools-name-service.yml

kube-delete:
	kubectl delete all --all

kube-port-forward-db:
	kubectl port-forward service/tools-name port:port
```

##

**<p align="center"> [Top](#title) </p>**