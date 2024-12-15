```shell
mvn clean package -DskipTests=true 
```

```shell
mvn test
```

```shell
docker compose --file ./docker-compose.yml --project-name grafana up --build -d
```

```text
http://localhost:8080/api/v1/application/status
```

Grafana: http://localhost:3000
Prometheus: http://localhost:9090
Tempo: http://localhost:3110
Loki: http://localhost:3100