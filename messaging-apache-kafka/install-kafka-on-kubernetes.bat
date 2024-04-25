REM ====================================================================================================================
REM Zookeeper
REM ====================================================================================================================
kubectl apply -f ./kube/zookeeper-deployment.yml
REM kubectl get deployments -n default
REM kubectl describe deployment zookeeper -n default

kubectl apply -f ./kube/zookeeper-service.yml
REM kubectl get service -n default
REM kubectl describe service zookeeper-service -n default

REM ====================================================================================================================
REM Kafka
REM ====================================================================================================================
kubectl apply -f ./kube/kafka-deployment.yml
REM kubectl get deployments -n default
REM kubectl describe deployment kafka -n default

kubectl apply -f ./kube/kafka-service.yml
REM kubectl get service -n default
REM kubectl describe service kafka-service -n default

REM ====================================================================================================================
REM Kafdrop
REM ====================================================================================================================
kubectl apply -f ./kube/kafdrop-deployment.yml
REM kubectl get deployments -n default
REM kubectl describe deployment kafdrop -n default

kubectl apply -f ./kube/kafdrop-service.yml
REM kubectl get service -n default
REM kubectl describe service kafdrop -n default

REM ====================================================================================================================
REM After Install
REM ====================================================================================================================
kubectl get all

REM ====================================================================================================================
REM Access from localhost
REM ====================================================================================================================
REM if you want to connect to kafdrop from localhost through the web browser or application use the following command
REM http://localhost:9000
kubectl port-forward service/kafdrop-service 9000:9000