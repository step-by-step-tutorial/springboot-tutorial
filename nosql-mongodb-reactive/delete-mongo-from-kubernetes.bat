kubectl delete all --all

REM delete mongo stuff
kubectl delete secrets mongo-secrets
kubectl delete persistentvolumeclaim mongo-pvc