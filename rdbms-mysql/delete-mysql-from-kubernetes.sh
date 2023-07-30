kubectl delete all --all

kubectl delete secrets mysql-secrets
kubectl delete configMap mysql-configmap
kubectl delete persistentvolumeclaim mysql-pvc

kubectl delete secrets adminer-secrets