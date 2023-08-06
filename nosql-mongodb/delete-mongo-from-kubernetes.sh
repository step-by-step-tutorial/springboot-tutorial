kubectl delete all --all

# delete mongo stuff
kubectl delete secrets mongo-secrets
kubectl delete persistentvolumeclaim mongo-pvc