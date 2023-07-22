docker --version
docker-compose --version
docker-machine --version

docker rm oracle --force
docker image rm container-registry.oracle.com/database/express:21.3.0-xe

docker compose --file ./docker-compose.yml --project-name oracledb up -d