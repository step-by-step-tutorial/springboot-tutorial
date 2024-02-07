#!/bin/bash

echo "E2ETest of Sample API"
# CRUD Operation

echo "==================================================================================================================="
echo "Scenario: Save a new sample"
NEW_SAMPLE_SAVE="{\"text\": \"example text\",\"code\": \"200\",\"datetime\": \"2020-10-10T01:00:00\"}"
SAMPLE_ID=$(curl -s -X POST -H "Content-Type: application/json" -d "$NEW_SAMPLE_SAVE" -L "http://localhost:8080/api/v1/samples")
echo "INPUT: $NEW_SAMPLE_SAVE"
echo "RESPONSE: $SAMPLE_ID"

echo
echo "==================================================================================================================="
echo "Scenario: Get one sample by given an ID"
FETCHED_SAMPLE_FIND=$(curl -s -X GET -H "Content-Type: application/json" -L "http://localhost:8080/api/v1/samples/$SAMPLE_ID")
echo "INPUT: $SAMPLE_ID"
echo "RESPONSE:"
echo "$FETCHED_SAMPLE_FIND" | jq

echo
echo "==================================================================================================================="
echo "Scenario: Update one sample"
NEW_SAMPLE_UPDATE="{\"text\": \"updated example text\",\"code\": \"201\",\"datetime\": \"2020-10-10T01:01:00\"}"
curl -si -X PUT -H "Content-Type: application/json" -d "$NEW_SAMPLE_UPDATE" -L "http://localhost:8080/api/v1/samples/$SAMPLE_ID"
FETCHED_SAMPLE_UPDATED=$(curl -s -X GET -H "Content-Type: application/json" -L "http://localhost:8080/api/v1/samples/$SAMPLE_ID")
echo "INPUT[1]: $SAMPLE_ID"
echo "INPUT[2]: $NEW_SAMPLE_UPDATE"
echo "RESPONSE: No Content"
echo "FETCHED_SAMPLE_UPDATED:"
echo "$FETCHED_SAMPLE_UPDATED" | jq

echo
echo "==================================================================================================================="
echo "Scenario: Update one sample with bad data"
UPDATE_SAMPLE_BAD_DATA="{\"id\": \"0\",\"text\": \"updated example text\",\"code\": \"201\",\"datetime\": \"2020-10-10T01:01:00\"}"
FETCHED_SAMPLE_UPDATE_ERROR=$(curl -s -X PUT -H "Content-Type: application/json" -d "$UPDATE_SAMPLE_BAD_DATA" -L "http://localhost:8080/api/v1/samples/$SAMPLE_ID")
echo "INPUT[1]: $SAMPLE_ID"
echo "INPUT[2]: $UPDATE_SAMPLE_BAD_DATA"
echo "RESPONSE: $FETCHED_SAMPLE_UPDATE_ERROR"

echo
echo "==================================================================================================================="
echo "Scenario: Delete one sample by given an ID"
curl -si -X DELETE -H "Content-Type: application/json" -L "http://localhost:8080/api/v1/samples/$SAMPLE_ID"
FETCHED_SAMPLE_DELETED=$(curl -s -X GET -H "Content-Type: application/json" -L "http://localhost:8080/api/v1/samples/$SAMPLE_ID")
echo "INPUT: $SAMPLE_ID"
echo "RESPONSE: No Content"
echo "FETCHED_SAMPLE_DELETED:"
echo "$FETCHED_SAMPLE_DELETED" | jq

echo
echo "==================================================================================================================="
echo "Scenario: Save a list of sample"
NEW_SAMPLE_BATCH="[{\"text\": \"example text 1\",\"code\": \"200\",\"datetime\": \"2020-10-10T01:00:00\"},{\"text\": \"example text 2\",\"code\": \"201\",\"datetime\": \"2020-10-10T01:00:00\"}]"
SAMPLE_ID_LIST=$(curl -s -X POST -H "Content-Type: application/json" -d "$NEW_SAMPLE_BATCH" -L "http://localhost:8080/api/v1/samples/batch")
echo "INPUT: $NEW_SAMPLE_BATCH"
echo "RESPONSE: $SAMPLE_ID_LIST"

echo
echo "==================================================================================================================="
echo "Scenario: Get all samples"
FETCHED_SAMPLE_FIND_ALL=$(curl -s -X GET -H "Content-Type: application/json" -L "http://localhost:8080/api/v1/samples")
echo "INPUT: nothing"
echo "RESPONSE:"
echo "$FETCHED_SAMPLE_FIND_ALL" | jq

echo
echo "==================================================================================================================="
echo "Scenario: Truncate samples"
curl -si -X DELETE -H "Content-Type: application/json" -L "http://localhost:8080/api/v1/samples"
FETCHED_SAMPLE_TRUNCATED=$(curl -s -X GET -H "Content-Type: application/json" -L "http://localhost:8080/api/v1/samples")
echo "FETCHED_SAMPLE_TRUNCATED: $FETCHED_SAMPLE_TRUNCATED"
echo
echo "==================================================================================================================="
