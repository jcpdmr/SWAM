# curl -G -d "paramTest1=ciao" -d "paramTest2=prova" http://localhost:8080/api/workflow

curl -X GET http://localhost:8080/api/workflow/catalog
echo "\n"
# curl -X GET http://localhost:8080/api/workflow/catalog/12
# echo "\n"
# curl -X GET http://localhost:8080/api/workflow/operation
# echo "\n"

# curl -X PUT http://localhost:8080/api/workflow/operation/1
# echo "\n"

# curl -X DELETE http://localhost:8080/api/workflow/operation/2
# echo "\n"

# curl -X POST http://localhost:8080/api/workflow/catalog\
#      -H "Content-Type: application/json" \
#      -d @./client-data.json

# echo "\n"

# curl -X POST http://localhost:8080/api/workflow/operation\
#      -H "Content-Type: application/json" \
#      -d @./client-data.json

# echo "\n"