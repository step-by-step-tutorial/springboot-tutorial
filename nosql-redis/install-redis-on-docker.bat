docker --version
docker-compose --version
docker-machine --version

docker rm redis --force
docker image rm redis:latest

docker compose --file .\nosql-redis\docker-compose.yml --project-name redis up --build -d
