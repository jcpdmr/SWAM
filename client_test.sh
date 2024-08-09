# curl -G -d "paramTest1=ciao" -d "paramTest2=prova" http://localhost:8080/test-api-get

curl -X POST http://localhost:8080/test-api-post \
     -H "Content-Type: application/json" \
     -d @./client-data.json