curl -G -d "paramTest1=ciao" -d "paramTest2=prova" http://localhost:8080/api/dev/workflow

# curl -X POST http://localhost:8080/api/dev/workflow \
#      -H "Content-Type: application/json" \
#      -d @./client-data.json