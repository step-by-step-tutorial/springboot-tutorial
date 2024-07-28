build:
	mvn clean package -DskipTests=true

test:
	mvn test

run:
	mvn spring-boot:run

docker-build:
	docker build -t samanalishiri/securityauthenticateinmemory:latest .

docker-deploy:
	docker run \
	--name securityauthenticateinmemory \
	-p 8080:8080 \
	-h securityauthenticateinmemory \
	-e APP_HOST=0.0.0.0 \
	-e APP_PORT=8080 \
	-itd samanalishiri/securityauthenticateinmemory:latest

docker-compose-deploy:
	docker compose --file ./docker-compose.yml --project-name securityauthenticateinmemory up --build -d

docker-remove-container:
	docker rm securityauthenticateinmemory --force

docker-remove-image:
	docker image rm samanalishiri/securityauthenticateinmemory:latest

kube-deploy:
	kubectl apply -f ./kube/app-deployment.yml
	kubectl apply -f ./kube/app-service.yml

kube-delete:
	kubectl delete all --all

kube-port-forward-app:
	kubectl port-forward service/securityauthenticateinmemory 8080:8080