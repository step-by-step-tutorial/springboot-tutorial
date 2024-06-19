# ======================================================================================================================
# Artemis
# ======================================================================================================================
kubectl apply -f ./kube/artemis-deployment.yml
# kubectl get deployments -n default
# kubectl describe deployment artemis -n default

kubectl apply -f ./kube/artemis-service.yml
# kubectl get service -n default
# kubectl describe service artemis -n default

# ======================================================================================================================
# After Install
# ======================================================================================================================
kubectl get all

# ======================================================================================================================
# Access from localhost
# ======================================================================================================================
# if you want to connect to artemis from localhost through the web browser or application use the following command
# http://localhost:8161
kubectl port-forward service/artemis 8161:8161