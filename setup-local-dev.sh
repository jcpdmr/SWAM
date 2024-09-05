#!/bin/bash

# Note: be sure to configure the right path to .env file
source .env

BASE_SERVICES=("rabbitmq" "mongodb_catalog" "mongodb_operation" "config-server")
MICROSERVICES=("catalog" "operation" "analysis" "gateway")

# Function used to install a JAR in local Maven repository
install_jar() {
    local group_id="$1"
    local artifact_id="$2"
    local version="$3"
    local jar_path="$4"
    
    # Build the jar path in the local Maven repository
    local local_repo_path="$HOME/.m2/repository/$(echo $group_id | tr '.' '/')/$artifact_id/$version/$artifact_id-$version.jar"

    # Check if the jar already exists in the local repository
    if [ -f "$local_repo_path" ]; then
        echo "$artifact_id jar is already installed in local Maven repository."
    else
        echo "$artifact_id jar is not installed in local Maven repository. Proceeding to install..."
        
        # Verify that the jar file exists in the lib folder.
        if [ -f "$jar_path" ]; then
            # Install the jar in the local repository
            mvn install:install-file -Dfile="$jar_path" -DgroupId="$group_id" -DartifactId="$artifact_id" -Dversion="$version" -Dpackaging=jar
            
            if [ $? -eq 0 ]; then
                echo "$artifact_id jar was successfully installed in the local Maven repository."
            else
                echo "An error occurred while installing the $artifact_id jar."
            fi
        else
            echo "Error: The file $jar_path does not exist."
            exit 1
        fi
    fi
}

setup_dependency(){
  install_jar "com.eulero" "eulero" "1.0" ${EULERO_JAR_PATH}
  install_jar "com.sirio" "sirio" "1.0" ${SIRIO_JAR_PATH}
}

check_mongodb() {
  local service=$1
  local retries=30
  local count=0

  while [ $count -lt $retries ]; do
    echo "Verifica se MongoDB ($service) è pronto... ($((count + 1))/$retries)"

    # Check if mongodb instance is ready
    if docker compose exec -T "$service" mongosh --eval "db.runCommand({ ping: 1 })" --quiet; then
      echo "$service è pronto a ricevere connessioni."
      return 0
    fi

    sleep 1
    ((count++))
  done

  echo "$service non è pronto dopo $retries tentativi."
  return 1
}

check_config_server() {
  local url="http://localhost:8888/catalog/local"
  local max_attempts=10
  local attempt=1

  while [ $attempt -le $max_attempts ]; do
    echo "Controllo se il Config Server è pronto (tentativo $attempt di $max_attempts)..."

    # Esegui la richiesta HTTP al Config Server
    if curl -s --head "$url" | head -n 1 | grep "HTTP/1.[01] 200" > /dev/null; then
      echo "Il Config Server è pronto."
      return 0
    fi

    # Aspetta prima di tentare di nuovo
    sleep 1
    attempt=$((attempt + 1))
  done

  echo "Il Config Server non è pronto dopo $max_attempts tentativi."
  return 1
}

is_service_running() {
  local service=$1
  # Get service's container id
  local container_id=$(docker compose ps -q "$service")
  
  # Check if the container exists and it's running
  if [ -n "$container_id" ]; then
    local status=$(docker inspect -f '{{.State.Status}}' "$container_id")
    [ "$status" == "running" ]
  else
    return 1
  fi
}

is_any_service_running(){
    local active_containers
    active_containers=$(docker compose ps --filter "status=running" --services)

    if [[ -z "$active_containers" ]]; then
        return 1  
    else
        return 0  
    fi
}

start_services(){
    local services=("$@")
    for service in "${services[@]}"; do
        # Check if the microservice is running, otherwise start it
        if ! is_service_running "$service"; then
            echo "Starting service: $service"
            docker compose up -d "$service"
        fi
    done
}

drop_all_mongoDB_databases(){
  docker compose exec mongodb_catalog mongosh \
  --username $MONGO_USER \
  --password $MONGO_PSW \
  --authenticationDatabase admin \
  --eval "db.getSiblingDB('$MONGO_DB_NAME').dropDatabase()"

  docker compose exec mongodb_operation mongosh \
  --username $MONGO_USER \
  --password $MONGO_PSW \
  --authenticationDatabase admin \
  --eval "db.getSiblingDB('$MONGO_DB_NAME').dropDatabase()"
}

kill_old_spring_instance(){
  ps aux | grep 'spring-boot:run' | awk '{print $2}' | while read pid; do
    if [[ -n "$pid" ]]; then
        echo "Terminazione del processo PID: $pid"
        kill "$pid" 2>/dev/null || echo "Errore nella terminazione del processo PID: $pid"
    fi
  done
}

base_setup(){
    setup_dependency

    start_services "${BASE_SERVICES[@]}"
    
    check_mongodb "mongodb_catalog" &
    PID2=$!
    check_mongodb "mongodb_operation" &
    PID3=$!

    wait $PID2
    wait $PID3

    drop_all_mongoDB_databases

    check_config_server
}

setup_env(){
  kill_old_spring_instance

  base_setup
}

# setup_unit_test(){

# }

setup_integration_test(){
    if is_any_service_running; then
        docker compose down
    fi

    # Multithread compilation with maven
    ./mvnw clean install --threads 1C -DskipTests

    base_setup

    start_services "${MICROSERVICES[@]}"
}


usage() {
  echo "Usage: $0 [-s] [-m]"
  echo "  -s  Setup local env"
  echo "  -h  This message"
  echo " check script for additional commands"
  exit 1
}

# Options parsing
while getopts "sabh" opt; do
  case ${opt} in
    s )
      setup_env
      ;;
    a )
      setup_unit_test
      ;;
    b )
      setup_integration_test
      ;;
    h )
      usage
      ;;
    \? )
      usage
      ;;
  esac
done

# Checks if no arguments have been passed
if [ $# -eq 0 ]; then
  usage
  exit 1
fi