docker rm restfulwebapi --force
docker image rm samanalishiri/restfulwebapi:latest

docker build -t samanalishiri/restfulwebapi:latest .

docker run \
--name restfulwebapi \
-p 8080:8080 \
-h restfulwebapi \
-e APP_HOST=0.0.0.0 \
-e APP_PORT=8080 \
-itd samanalishiri/restfulwebapi:latest