clean:
	docker rm kafka --force
	docker rm zookeeper --force
	docker rm kafdrop --force
	docker image rm confluentinc/cp-zookeeper:latest
	docker image rm confluentinc/cp-kafka:latest
	docker image rm obsidiandynamics/kafdrop:latest

install:
	docker compose --file docker-compose.yml --project-name kafka up -d

clean-install:
	docker rm kafka --force
	docker rm zookeeper --force
	docker rm kafdrop --force
	docker image rm confluentinc/cp-zookeeper:latest
	docker image rm confluentinc/cp-kafka:latest
	docker image rm obsidiandynamics/kafdrop:latest
	docker compose --file docker-compose.yml --project-name kafka up -d
