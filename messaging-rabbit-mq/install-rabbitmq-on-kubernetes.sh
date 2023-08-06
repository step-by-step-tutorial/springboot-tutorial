# ======================================================================================================================
# Rabbitmq
# ======================================================================================================================
kubectl apply -f ./kube/rabbitmq-secrets.yml
# kubectl describe secret rabbitmq-secrets -n default
# kubectl get secret rabbitmq-secrets -n default -o yaml

kubectl apply -f ./kube/rabbitmq-deployment.yml
# kubectl get deployments -n default
# kubectl describe deployment rabbitmq -n default

kubectl apply -f ./kube/rabbitmq-service.yml
# kubectl get service -n default
# kubectl describe service rabbitmq -n default

# ======================================================================================================================
# After Install
# ======================================================================================================================
kubectl get all

# ======================================================================================================================
# Access from localhost
# ======================================================================================================================

# if you want to connect to rabbitmq from localhost through the web browser or application use the following command
# http://localhost:15672
kubectl port-forward service/rabbitmq 15672:15672
