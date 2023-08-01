# ======================================================================================================================
# MySQL
# ======================================================================================================================
kubectl apply -f ./kube/mysql-pvc.yml
# kubectl get pvc 
# kubectl describe pvc mysql-pvc

kubectl apply -f ./kube/mysql-configmap.yml
# kubectl describe configmap mysql-configmap -n default

kubectl apply -f ./kube/mysql-secrets.yml
# kubectl describe secret mysql-secrets -n default
# kubectl get secret mysql-secrets -n default -o yaml

kubectl apply -f ./kube/mysql-deployment.yml
# kubectl get deployments -n default
# kubectl describe deployment mysql -n default

kubectl apply -f ./kube/mysql-service.yml
# kubectl get service -n default
# kubectl describe service mysql -n default

# ======================================================================================================================
# Adminer
# ======================================================================================================================
kubectl apply -f ./kube/adminer-deployment.yml
# kubectl get deployments -n default
# kubectl describe deployment adminer -n default

kubectl apply -f ./kube/adminer-service.yml
# kubectl get services -n default
# kubectl describe service adminer -n default

# ======================================================================================================================
# Phpmyadmin
# ======================================================================================================================
# kubectl apply -f ./kube/phpmyadmin-deployment.yml
# kubectl get deployments -n default
# kubectl describe deployment phpmyadmin -n default

# kubectl apply -f ./kube/phpmyadmin-service.yml
# kubectl get services -n default
# kubectl describe service phpmyadmin -n default

# ======================================================================================================================
# After Install
# ======================================================================================================================
kubectl get all

# ======================================================================================================================
# Access from localhost
# ======================================================================================================================
# if you want to connect database from localhost through the application use the following command
kubectl port-forward service/mysql 3306:3306

# if you want to connect to adminer from localhost through the web browser use the following command
# http://localhost:8080
kubectl port-forward service/adminer 8080:8080

# if you want to connect to phpmyadmin from localhost through the web browser use the following command
# http://localhost:8080
# kubectl port-forward service/phpmyadmin 8080:80
