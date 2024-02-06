@echo off
cls
echo E2ETest of Sample API
REM ====================================================================================================================
REM CRUD Operation
REM ====================================================================================================================
echo:
echo ===================================================================================================================
echo Scenario: Save a new sample
set NEW_SAMPLE="{\"text\": \"example text\",\"code\": \"200\",\"datetime\": \"2020-10-10T01:00:00\"}"
for /f "delims=" %%i in ('curl -s -X POST -H "Content-Type: application/json" -d %NEW_SAMPLE% -L "http://localhost:8080/api/v1/samples"') do set SAMPLE_ID=%%i
echo INPUT: %NEW_SAMPLE%
echo RESPONSE: %SAMPLE_ID%

echo:
echo ===================================================================================================================
echo Scenario: Get one sample by given an ID
for /f "delims=" %%i in ('curl -s -X GET -H "Content-Type: application/json" -L "http://localhost:8080/api/v1/samples/%SAMPLE_ID%"') do set FETCHED_SAMPLE=%%i
echo INPUT: %SAMPLE_ID%
echo RESPONSE:
echo %FETCHED_SAMPLE% | jq

echo:
echo ===================================================================================================================
echo Scenario: Save a list of sample
set NEW_SAMPLE_LIST="[{\"text\": \"example text 1\",\"code\": \"200\",\"datetime\": \"2020-10-10T01:00:00\"},{\"text\": \"example text 2\",\"code\": \"201\",\"datetime\": \"2020-10-10T01:00:00\"}]"
for /f "delims=" %%i in ('curl -s -X POST -H "Content-Type: application/json" -d %NEW_SAMPLE_LIST% -L "http://localhost:8080/api/v1/samples/saveAll"') do set SAMPLE_ID_LIST=%%i
echo INPUT: %NEW_SAMPLE_LIST%
echo RESPONSE: %SAMPLE_ID_LIST%

echo:
echo ===================================================================================================================
echo Scenario: Get all samples
for /f "delims=" %%i in ('curl -s -X GET -H "Content-Type: application/json" -L "http://localhost:8080/api/v1/samples/findAll"') do set FETCHED_SAMPLE_LIST=%%i
echo INPUT: nothing
echo RESPONSE:
echo %FETCHED_SAMPLE_LIST% | jq

echo:
echo ===================================================================================================================
echo Scenario: Update one sample
set UPDATE_SAMPLE="{\"text\": \"updated example text\",\"code\": \"201\",\"datetime\": \"2020-10-10T01:01:00\"}"
curl -si -X PUT -H "Content-Type: application/json" -d %UPDATE_SAMPLE% -L "http://localhost:8080/api/v1/samples/%SAMPLE_ID%"
for /f "delims=" %%i in ('curl -s -X GET -H "Content-Type: application/json" -L "http://localhost:8080/api/v1/samples/%SAMPLE_ID%"') do set FETCHED_UPDATED_SAMPLE=%%i
echo INPUT[1]: %SAMPLE_ID%
echo INPUT[2]: %UPDATE_SAMPLE%
echo RESPONSE: No Content
echo FETCHED_UPDATED_SAMPLE:
echo %FETCHED_UPDATED_SAMPLE% | jq

echo:
echo ===================================================================================================================
echo Scenario: Update one sample with bad data
set UPDATE_SAMPLE_BAD_DATA="{\"id\": \"0\",\"text\": \"updated example text\",\"code\": \"201\",\"datetime\": \"2020-10-10T01:01:00\"}"
for /f "delims=" %%i in ('curl -s -X PUT -H "Content-Type: application/json" -d %UPDATE_SAMPLE_BAD_DATA% -L "http://localhost:8080/api/v1/samples/%SAMPLE_ID%"') do set FETCHED_UPDATED_SAMPLE_BAD_DATA=%%i
echo INPUT[1]: %SAMPLE_ID%
echo INPUT[2]: %UPDATE_SAMPLE_BAD_DATA%
echo RESPONSE: %FETCHED_UPDATED_SAMPLE_BAD_DATA%

echo:
echo ===================================================================================================================
echo Scenario: Delete one sample by given an ID
curl -si -X DELETE -H "Content-Type: application/json" -L "http://localhost:8080/api/v1/samples/%SAMPLE_ID%"
for /f "delims=" %%i in ('curl -s -X GET -H "Content-Type: application/json" -L "http://localhost:8080/api/v1/samples/%SAMPLE_ID%"') do set FETCHED_DELETED_SAMPLE=%%i
echo INPUT: %SAMPLE_ID%
echo RESPONSE: No Content
echo FETCHED_DELETED_SAMPLE:
echo %FETCHED_DELETED_SAMPLE% | jq

echo:
echo ===================================================================================================================
echo Scenario: Truncate samples
curl -si -X DELETE -H "Content-Type: application/json" -L "http://localhost:8080/api/v1/samples/truncate"
for /f "delims=" %%i in ('curl -s -X GET -H "Content-Type: application/json" -L "http://localhost:8080/api/v1/samples/findAll"') do set FETCHED_ANY_SAMPLE=%%i
echo FETCHED_ANY_SAMPLE: %FETCHED_ANY_SAMPLE%

echo:
echo ===================================================================================================================
