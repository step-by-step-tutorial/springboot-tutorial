docker rm oracle --force
docker image rm container-registry.oracle.com/database/express:21.3.0-xe

docker rm ords --force
docker image rm container-registry.oracle.com/database/ords:latest