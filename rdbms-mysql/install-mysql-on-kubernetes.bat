REM ====================================================================================================================
REM MySQL
REM ====================================================================================================================
kubectl apply -f ./kube/mysql-pvc.yml
REM kubectl get pvc
REM kubectl describe pvc mysql-pvc

kubectl apply -f ./kube/mysql-configmap.yml
REM kubectl describe configmap mysql-configmap -n default

kubectl apply -f ./kube/mysql-secrets.yml
REM kubectl describe secret mysql-secrets -n default
REM kubectl get secret mysql-secrets -n default -o yaml

kubectl apply -f ./kube/mysql-deployment.yml
REM kubectl get deployments -n default
REM kubectl describe deployment mysql -n default

kubectl apply -f ./kube/mysql-service.yml
REM kubectl get service -n default
REM kubectl describe service mysql -n default

REM ====================================================================================================================
REM Adminer
REM ====================================================================================================================
kubectl apply -f ./kube/adminer-deployment.yml
REM kubectl get deployments -n default
REM kubectl describe deployment adminer -n default

kubectl apply -f ./kube/adminer-service.yml
REM kubectl get services -n default
REM kubectl describe service adminer -n default

REM ====================================================================================================================
REM Phpmyadmin
REM ====================================================================================================================
REM kubectl apply -f ./kube/phpmyadmin-deployment.yml
REM kubectl get deployments -n default
REM kubectl describe deployment phpmyadmin -n default

REM kubectl apply -f ./kube/phpmyadmin-service.yml
REM kubectl get services -n default
REM kubectl describe service phpmyadmin -n default

REM ====================================================================================================================
REM After Install
REM ====================================================================================================================
kubectl get all

REM ====================================================================================================================
REM Access from localhost
REM ====================================================================================================================
REM if you want to connect database from localhost through the application use the following command
kubectl port-forward service/mysql 3306:3306

REM if you want to connect to adminer from localhost through the web browser use the following command
REM http://localhost:8080
kubectl port-forward service/adminer 8080:8080

REM if you want to connect to phpmyadmin from localhost through the web browser use the following command
REM http://localhost:8080
REM kubectl port-forward service/phpmyadmin 8080:80
