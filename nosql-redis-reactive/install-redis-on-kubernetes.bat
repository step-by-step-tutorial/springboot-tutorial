REM ====================================================================================================================
REM Redis
REM ====================================================================================================================
kubectl apply -f ./kube/redis-deployment.yml
REM kubectl get deployments -n default
REM kubectl describe deployment redis -n default

kubectl apply -f ./kube/redis-service.yml
REM kubectl get service -n default
REM kubectl describe service redis -n default

REM ====================================================================================================================
REM Redisinsight
REM ====================================================================================================================
kubectl apply -f ./kube/redisinsight-deployment.yml
REM kubectl get deployments -n default
REM kubectl describe deployment redisinsight -n default

kubectl apply -f ./kube/redisinsight-service.yml
REM kubectl get services -n default
REM kubectl describe service redisinsight -n default

REM ====================================================================================================================
REM Commander
REM ====================================================================================================================
REM kubectl apply -f ./kube/commander-deployment.yml
REM kubectl get deployments -n default
REM kubectl describe deployment commander -n default

REM kubectl apply -f ./kube/commander-service.yml
REM kubectl get services -n default
REM kubectl describe service commander -n default

REM ====================================================================================================================
REM After Install
REM ====================================================================================================================
kubectl get all

REM ====================================================================================================================
REM Access from localhost
REM ====================================================================================================================
REM if you want to connect database from localhost through the application use the following command
kubectl port-forward service/redis 6379:6379

REM if you want to connect to redisinsight from localhost through the web browser use the following command
REM http://localhost:8001
kubectl port-forward service/redisinsight 8001:8001

REM if you want to connect to commander from localhost through the web browser use the following command
REM http://localhost:8081
kubectl port-forward service/commander 8081:8081