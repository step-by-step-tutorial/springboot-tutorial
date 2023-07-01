REM check if docker is set up
docker --version
docker-compose --version
docker-machine --version

REM mysql, adminer =====================================================================================================
docker rm mysql --force
docker image rm mysql:8.0

docker rm adminer --force
docker image rm adminer

REM mysql, pgadmin, adminer ============================================================================================
docker rm postgresql --force
docker image rm postgres:13.9-alpine

docker rm pgadmin --force
docker image rm dpage/pgadmin4

docker rm adminer --force
docker image rm adminer

REM redis ==============================================================================================================
docker rm redis --force
docker image rm redis:latest

REM mongo, mongo-express ===============================================================================================
docker rm mongo --force
docker image rm mongo

docker rm mongo-express --force
docker image rm mongo-express

REM apache activemq artemis ============================================================================================
docker rm artemis --force
docker image rm apache/activemq-artemis:latest

REM oracle db ============================================================================================
docker rm oracle --force
docker image rm container-registry.oracle.com/database/express:21.3.0-xe
