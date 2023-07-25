docker --version
docker-compose --version
docker-machine --version

docker rm oracle --force
docker image rm container-registry.oracle.com/database/express:21.3.0-xe

docker rm adminer --force
docker image rm adminer

mkdir ords_secrets
mkdir ords_config
echo CONN_STRING="sys as sysdba/password@oracle:1521/xepdb1" > ords_secrets/conn_string.txt

docker compose --file ./docker-compose.yml --project-name oracledb up -d