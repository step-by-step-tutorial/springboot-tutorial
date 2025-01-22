build:
	mvn clean package -DskipTests=true

test:
	mvn test

run:
	mvn spring-boot:run

DockerComposeDeploy:
	docker compose --file docker-compose.yml --project-name dev-env up --build -d

DockerComposeDown:
	docker compose --file docker-compose.yml --project-name dev-env down

KubernetesDeploy:
	docker build -t samanalishiri/application:latest .
	kubectl apply -f ./kube/mysql.yml
	kubectl apply -f ./kube/kafka.yml
	kubectl apply -f ./kube/debezium.yml
	kubectl apply -f ./kube/application.yml

KubernetesDown:
	kubectl delete all --all
	kubectl delete secrets mysql-secrets
	kubectl delete configMap mysql-configmap
	kubectl delete persistentvolumeclaim mysql-pvc
	docker image rm samanalishiri/application:latest
