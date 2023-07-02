docker --version
docker-compose --version
docker-machine --version

docker rm rabbitmq --force
docker image rm rabbitmq:management

docker compose --file docker-compose.yml --project-name rabbitmq up --build -d
