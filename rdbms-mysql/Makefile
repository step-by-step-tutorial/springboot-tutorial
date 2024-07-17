build:
	mvn clean package -DskipTests=true

test:
	mvn test

run:
	mvn spring-boot:run

docker-compose-deploy:
	docker compose --file docker-compose.yml --project-name mysql up --build -d

docker-remove-container:
	docker rm mysql --force
	docker rm mysql-workbench- --force
	docker rm adminer --force
	docker rm phpmyadmin --force

docker-remove-image:
	docker image rm mysql:8.0
	docker image rm lscr.io/linuxserver/mysql-workbench:latest
	docker image rm adminer
	docker image rm phpmyadmin

kube-deploy:
	kubectl apply -f ./kube/mysql-pvc.yml
	kubectl apply -f ./kube/mysql-configmap.yml
	kubectl apply -f ./kube/mysql-secrets.yml
	kubectl apply -f ./kube/mysql-deployment.yml
	kubectl apply -f ./kube/mysql-service.yml
	kubectl apply -f ./kube/mysql-workbench-deployment.yml
	kubectl apply -f ./kube/mysql-workbench-service.yml
	kubectl apply -f ./kube/adminer-deployment.yml
	kubectl apply -f ./kube/adminer-service.yml
	kubectl apply -f ./kube/phpmyadmin-deployment.yml
	kubectl apply -f ./kube/phpmyadmin-service.yml

kube-delete:
	kubectl delete all --all
	kubectl delete secrets mysql-secrets
	kubectl delete configMap mysql-configmap
	kubectl delete persistentvolumeclaim mysql-pvc

kube-port-forward-mysql:
	kubectl port-forward service/mysql 3306:3306

kube-port-forward-adminer:
	kubectl port-forward service/adminer 8080:8080

kube-port-forward-phpmyadmin:
	kubectl port-forward service/phpmyadmin 8081:80

kube-port-forward-mysql-workbench:
	kubectl port-forward service/mysql-workbench 3000:3000