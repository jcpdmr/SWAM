# curl -G -d "paramTest1=ciao" -d "paramTest2=prova" http://localhost:8080/api/workflow

curl -X GET http://localhost:8080/api/workflow/catalog
echo ""
# curl -X GET http://localhost:8080/api/workflow/catalog/66c5c2a377c20125e95973b9
# echo ""
# curl -X GET http://localhost:8080/api/workflow/operation
# echo ""

# curl -X PUT http://localhost:8080/api/workflow/operation/1
# echo ""

# curl -X DELETE http://localhost:8080/api/workflow/operation/2
# echo ""

# curl -X POST http://localhost:8080/api/workflow/catalog\
#      -H "Content-Type: application/json" \
#      -d @./client-data.json
# curl -X POST http://localhost:8080/api/workflow/catalog\
#      -H "Content-Type: application/json" \
#      -d @./client-data.json

# echo ""

# curl -X POST http://localhost:8080/api/workflow/operation\
#      -H "Content-Type: application/json" \
#      -d @./client-data.json

# echo ""