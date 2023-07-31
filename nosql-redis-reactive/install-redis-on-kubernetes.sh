kubectl apply -f ./kube/redis-deployment.yml
# kubectl get deployments -n default
# kubectl describe deployment redis -n default

kubectl apply -f ./kube/redis-service.yml
# kubectl get service -n default
# kubectl describe service redis -n default

kubectl apply -f ./kube/redisinsight-deployment.yml
# kubectl get deployments -n default
# kubectl describe deployment redisinsight -n default

kubectl apply -f ./kube/redisinsight-service.yml
# kubectl get services -n default
# kubectl describe service redisinsight -n default

kubectl get all

# if you want to connect to redisinsight from localhost through the web browser use the following command
# http://localhost:8001
kubectl port-forward service/redisinsight 8001:8001

# if you want to connect database from localhost through the application use the following command
kubectl port-forward service/redis 6379:6379