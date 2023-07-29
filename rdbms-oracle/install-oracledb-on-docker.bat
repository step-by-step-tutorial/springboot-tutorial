mkdir ords_config
mkdir ords_secrets
echo CONN_STRING=^"sys as sysdba/password@oracle:1521/xepdb1^" > ords_secrets/conn_string.txt

docker compose --file .\docker-compose.yml --project-name oracledb up --build -d