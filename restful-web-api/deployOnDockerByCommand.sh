docker rm restful-web-services --force
docker image rm samanalishiri/restful-web-services:latest

docker build -t samanalishiri/restful-web-services:latest .

docker run \
--name restful-web-services \
-p 8080:8080 \
-h restful-web-services \
-e APP_HOST=0.0.0.0 \
-e APP_PORT=8080 \
-itd samanalishiri/restful-web-services:latest