docker --version
docker-compose --version
docker-machine --version

docker rm mysql --force
docker image rm mysql:8.0

docker rm adminer --force
docker image rm adminer

docker compose --file ./docker-compose.yml --project-name mysql up --build -d
