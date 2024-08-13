# curl -G -d "paramTest1=ciao" -d "paramTest2=prova" http://localhost:8080/api/workflow

curl -X GET http://localhost:8080/api/workflow/template
# curl -X GET http://localhost:8080/api/workflow/template/12
# curl -X GET http://localhost:8080/api/workflow/12/product/template
# curl -X GET http://localhost:8080/api/workflow/12/product/template/5
# curl -X GET http://localhost:8080/api/workflow/12/product/5/analysis/template
# curl -X GET http://localhost:8080/api/workflow/12/product/5/analysis/template/10

# curl -X POST http://localhost:8080/api/workflow\
#      -H "Content-Type: application/json" \
#      -d @./client-data.json