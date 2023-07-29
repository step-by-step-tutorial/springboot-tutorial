docker rm postgresql --force
docker image rm postgres:13.9-alpine

docker rm pgadmin --force
docker image rm dpage/pgadmin4

REM docker rm adminer --force
REM docker image rm adminer
