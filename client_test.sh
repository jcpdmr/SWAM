# curl -G -d "paramTest1=ciao" -d "paramTest2=prova" http://localhost:8080/api/workflow/catalog/test
# echo ""

# curl -X GET http://localhost:8080/api/workflow/catalog
# echo ""

# curl -X GET http://localhost:8080/api/workflow/catalog/test
# echo ""

# curl -X GET http://localhost:8080/api/workflow/tobeinstantiated/66cb4992a20535208e0238b8
# echo ""

# curl -X GET http://localhost:8080/api/workflow/tobeinstantiated/66ca0b8e70c55e1ab83b5eb4
# echo ""

# curl -X GET http://localhost:8080/api/workflow/operation
# echo ""

# curl -X PUT http://localhost:8080/api/workflow/operation/1
# echo ""

# curl -X POST http://localhost:8080/api/workflow/operation/1
# echo ""

# curl -X DELETE http://localhost:8080/api/workflow/operation/2
# echo ""

# curl -X POST http://localhost:8080/api/workflow/catalog/1\
#      -H "Content-Type: application/json" \
#      -d @./client-data.json
# echo ""

# curl -X POST http://localhost:8080/api/workflow/catalog\
#      -H "Content-Type: application/json" \
#      -d @./client-data.json
# echo ""

# curl -X DELETE http://localhost:8080/api/workflow/catalog/66cb4e08b48c8d6cc4aec995
# echo ""

# curl -X POST http://localhost:8080/api/workflow/operation\
#      -H "Content-Type: application/json" \
#      -d @./client-data.json

# echo ""

# curl -X PUT http://localhost:8080/api/workflow/catalog/66cb4ed6b48c8d6cc4aec99c \
#      -H "Content-Type: application/json" \
#      -d @./client-data.json

# echo ""

# curl -X GET http://localhost:8080/api/workflow/catalog/test/product
# echo ""

# curl -X GET http://localhost:8080/api/workflow/catalog/66cb6ea148269f54767faa99/product/v0
# echo ""

# curl -X PUT http://localhost:8080/api/workflow/catalog/test/product/v0 \
#      -H "Content-Type: application/json" \
#      -d @./productData2.json

# echo ""

# curl -X DELETE http://localhost:8080/api/workflow/catalog/test/product/v5
# echo ""

# curl -X GET http://localhost:8080/api/analysis/catalog/test
# echo ""

curl -G -d "format=svg" http://localhost:8080/api/analysis/catalog/test
echo ""