build:
	mvn clean package -DskipTests=true

test:
	mvn test

run:
	mvn spring-boot:run

docker-build:
	docker build -t samanalishiri/securityoauth2client:latest .

docker-deploy:
	docker run \
	--name securityoauth2client \
	-p 8081:8081 \
	-h securityoauth2client \
	-e APP_HOST=0.0.0.0 \
	-e APP_PORT=8081 \
	-e APP_PROFILES=h2 \
	-itd samanalishiri/securityoauth2client:latest

