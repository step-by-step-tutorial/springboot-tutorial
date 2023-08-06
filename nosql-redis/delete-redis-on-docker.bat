docker rm redis --force
docker image rm redis:latest
docker rm redisinsight --force
docker image rm redislabs/redisinsight:latest

REM docker rm commander --force
REM docker image rm rediscommander/redis-commander:latest