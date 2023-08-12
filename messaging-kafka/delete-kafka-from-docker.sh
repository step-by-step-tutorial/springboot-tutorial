docker rm zookeeper --force
docker rm kafka --force
docker rm kafdrop --force

docker image rm confluentinc/cp-zookeeper:latest
docker image rm confluentinc/cp-kafka:latest
docker image rm obsidiandynamics/kafdrop:latest

