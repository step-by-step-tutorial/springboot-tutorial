#   ====================================================================================================================
#   CRUD Operation
#   ====================================================================================================================
echo ===================================================================================================================
ID=$(curl --location --request POST "http://localhost:8080/api/v1/samples" --header "Content-Type: application/json" --data "{\"text\": \"example text\",\"code\": \"200\",\"datetime\": \"2020-10-10T01:00:00\"}")
echo save sample: "$ID"
echo ===================================================================================================================
SAMPLES=$(curl --location --request GET "http://localhost:8080/api/v1/samples")
echo get all samples: "$SAMPLES"
echo ===================================================================================================================
SAMPLE=$(curl --location --request GET "http://localhost:8080/api/v1/samples/1")
echo get one sample by ID: "$SAMPLE"
echo ===================================================================================================================
UPDATE_RESPONSE=$(curl --location --request PUT "http://localhost:8080/api/v1/samples/1" --header "Content-Type: application/json" --data "{\"text\": \"updated example text\",\"code\": \"201\",\"datetime\": \"2020-10-10T01:01:00\"}")
echo update sample: "$UPDATE_RESPONSE"
echo ===================================================================================================================
UPDATE_RESPONSE_ERROR=$(curl --location --request PUT "http://localhost:8080/api/v1/samples/1" --header "Content-Type: application/json" --data "{\"id\": \"0\",\"text\": \"updated example text\",\"code\": \"201\",\"datetime\": \"2020-10-10T01:01:00\"}")
echo update sample with bad data: "$UPDATE_RESPONSE_ERROR"
echo ===================================================================================================================
DELETE_RESPONSE=$(curl --location --request DELETE "http://localhost:8080/api/v1/samples/1")
echo delete sample by ID: "$DELETE_RESPONSE"
echo ===================================================================================================================
IDENTIFIERS=$(curl --location --request POST "http://localhost:8080/api/v1/samples/saveAll" --header "Content-Type: application/json" --data "[{\"text\": \"example text 1\",\"code\": \"200\",\"datetime\": \"2020-10-10T01:00:00\"},{\"text\": \"example text 2\",\"code\": \"201\",\"datetime\": \"2020-10-10T01:00:00\"}]")
echo save a list of sample: "$IDENTIFIERS"
echo ===================================================================================================================
DELETE_ALL_RESPONSE=$(curl --location --request DELETE "http://localhost:8080/api/v1/samples")
echo delete all samples: "$DELETE_ALL_RESPONSE"
echo ===================================================================================================================
