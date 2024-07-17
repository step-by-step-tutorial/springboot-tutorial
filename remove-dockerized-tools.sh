# remove containers
docker rm -f \
  mysql \
  mysql-workbench \
  postgresql \
  pgadmin \
  oracle \
  ords \
  adminer \
  phpmyadmin \
  redis \
  redisinsight \
  commander \
  mongo \
  mongo-express \
  rabbitmq \
  artemis \
  zookeeper \
  kafka \
  kafdrop

# remove images
docker rmi \
  mysql:8.0 \
  lscr.io/linuxserver/mysql-workbench:latest \
  postgres:13.9-alpine \
  dpage/pgadmin4 \
  container-registry.oracle.com/database/express:21.3.0-xe \
  container-registry.oracle.com/database/ords:latest \
  adminer \
  phpmyadmin \
  redis:latest \
  redislabs/redisinsight:latest \
  rediscommander/redis-commander:latest \
  mongo \
  mongo-express \
  rabbitmq:management \
  apache/activemq-artemis:latest \
  docker.io/bitnami/zookeeper \
  docker.io/bitnami/kafka \
  obsidiandynamics/kafdrop:latest