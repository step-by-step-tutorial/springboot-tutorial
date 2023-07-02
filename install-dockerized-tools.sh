# check if docker is set up
docker --version
docker-compose --version
docker-machine --version
# ====================================================================================================================

# mysql ==============================================================================================================
docker rm mysql --force
docker image rm mysql:8.0
# ====================================================================================================================

# postgresql =========================================================================================================
docker rm postgresql --force
docker image rm postgres:13.9-alpine
# ====================================================================================================================

# oracle db ==========================================================================================================
docker rm oracle --force
docker image rm container-registry.oracle.com/database/express:21.3.0-xe
# ====================================================================================================================

# adminer ============================================================================================================
docker rm adminer --force
docker image rm adminer
# ====================================================================================================================

# pgadmin ============================================================================================================
docker rm pgadmin --force
docker image rm dpage/pgadmin4
# ====================================================================================================================

# redis ==============================================================================================================
docker rm redis --force
docker image rm redis:latest
# ====================================================================================================================

# mongo ==============================================================================================================
docker rm mongo --force
docker image rm mongo

# ====================================================================================================================

# mongo-express ======================================================================================================
docker rm mongo-express --force
docker image rm mongo-express
# ====================================================================================================================

# apache activemq artemis ============================================================================================
docker rm artemis --force
docker image rm apache/activemq-artemis:latest
# ====================================================================================================================

# rabbitmq ===========================================================================================================
docker rm rabbitmq --force
docker image rm rabbitmq:management
# ====================================================================================================================

docker compose --file ./docker-compose.yml --project-name springboot_tutorial up -d
