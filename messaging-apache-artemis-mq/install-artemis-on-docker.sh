docker --version
docker-compose --version
docker-machine --version

docker rm artemis --force
docker image rm apache/activemq-artemis:latest

docker compose --file ./docker/docker-compose.yml --project-name artemis up --build -d
