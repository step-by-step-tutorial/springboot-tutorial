docker --version
docker-compose --version
docker-machine --version

docker rm redis --force
docker image rm redis:latest
docker rm redisinsight --force
docker image rm redislabs/redisinsight:latest

docker compose --file ./docker-compose.yml --project-name redis up --build -d
