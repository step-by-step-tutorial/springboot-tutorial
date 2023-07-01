docker --version
docker-compose --version
docker-machine --version

docker rm postgresql --force
docker image rm postgres:13.9-alpine

docker rm pgadmin --force
docker image rm dpage/pgadmin4

docker rm adminer --force
docker image rm adminer

docker compose --file ./rdbms-postgresql/docker-compose.yml --project-name postgresql up --build -d
