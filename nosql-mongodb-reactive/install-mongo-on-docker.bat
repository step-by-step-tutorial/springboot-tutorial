docker --version
docker-compose --version
docker-machine --version

docker rm mongo --force
docker image rm mongo

docker rm mongo-express --force
docker image rm mongo-express

docker compose --file .\nosql-mongo\docker-compose.yml --project-name mongo up --build -d
