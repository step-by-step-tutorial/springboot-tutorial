docker rm redis --force
docker image rm redis:latest
docker rm redisinsight --force
docker image rm redislabs/redisinsight:latest

# docker rm commander --force
# docker image rm rediscommander/redis-commander:latest