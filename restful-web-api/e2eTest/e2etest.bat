@echo off
REM ====================================================================================================================
REM CRUD Operation
REM ====================================================================================================================
echo:
echo ===================================================================================================================
echo save sample
curl --location --request POST "http://localhost:8080/api/v1/samples" --header "Content-Type: application/json" --data "{\"text\": \"example text\",\"code\": \"200\",\"datetime\": \"2020-10-10T01:00:00\"}"

echo:
echo ===================================================================================================================
echo get all samples
curl --location --request GET "http://localhost:8080/api/v1/samples"

echo:
echo ===================================================================================================================
echo get one sample by ID
curl --location --request GET "http://localhost:8080/api/v1/samples/1"

echo:
echo ===================================================================================================================
echo update sample
curl --location --request PUT "http://localhost:8080/api/v1/samples/1" --header "Content-Type: application/json" --data "{\"text\": \"updated example text\",\"code\": \"201\",\"datetime\": \"2020-10-10T01:01:00\"}"

echo:
echo ===================================================================================================================
echo update sample with bad data
curl --location --request PUT "http://localhost:8080/api/v1/samples/1" --header "Content-Type: application/json" --data "{\"id\": \"0\",\"text\": \"updated example text\",\"code\": \"201\",\"datetime\": \"2020-10-10T01:01:00\"}"

echo:
echo ===================================================================================================================
echo delete sample by ID
curl --location --request DELETE "http://localhost:8080/api/v1/samples/1"

echo:
echo ===================================================================================================================
echo save a list of sample
curl --location --request POST "http://localhost:8080/api/v1/samples/saveAll" --header "Content-Type: application/json" --data "[{\"text\": \"example text 1\",\"code\": \"200\",\"datetime\": \"2020-10-10T01:00:00\"},{\"text\": \"example text 2\",\"code\": \"201\",\"datetime\": \"2020-10-10T01:00:00\"}]"

echo:
echo ===================================================================================================================
echo delete all samples
curl --location --request DELETE "http://localhost:8080/api/v1/samples"
echo:
echo ===================================================================================================================
