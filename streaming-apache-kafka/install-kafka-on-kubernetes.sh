# ======================================================================================================================
# Zookeeper
# ======================================================================================================================
kubectl apply -f ./kube/zookeeper-deployment.yml
# kubectl get deployments -n default
# kubectl describe deployment zookeeper -n default

kubectl apply -f ./kube/zookeeper-service.yml
# kubectl get service -n default
# kubectl describe service zookeeper-service -n default

# ======================================================================================================================
# Kafka
# ======================================================================================================================
kubectl apply -f ./kube/kafka-deployment.yml
# kubectl get deployments -n default
# kubectl describe deployment kafka -n default

kubectl apply -f ./kube/kafka-service.yml
# kubectl get service -n default
# kubectl describe service kafka-service -n default

# ======================================================================================================================
# Kafdrop
# ======================================================================================================================
kubectl apply -f ./kube/kafdrop-deployment.yml
# kubectl get deployments -n default
# kubectl describe deployment kafdrop -n default

kubectl apply -f ./kube/kafdrop-service.yml
# kubectl get service -n default
# kubectl describe service kafdrop -n default

# ======================================================================================================================
# After Install
# ======================================================================================================================
kubectl get all

# ======================================================================================================================
# Access from localhost
# ======================================================================================================================
# if you want to connect to kafdrop from localhost through the web browser or application use the following command
# http://localhost:9000
kubectl port-forward service/kafdrop-service 9000:9000