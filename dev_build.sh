#!/bin/bash

# Note: be sure to configure the right path to .env file
source .env

CONFIG_SERVER="config-server"

BASE_SERVICES=("rabbitmq" "mongodb_catalog" "mongodb_operation" "config-server")

MICROSERVICES=(${MICROSERVICE_CATALOG_NAME} ${MICROSERVICE_OPERATION_NAME} ${MICROSERVICE_ANALYSIS_NAME} ${MICROSERVICE_GATEWAY_NAME})
MICROSERVICES_TO_LOG=(${MICROSERVICE_CATALOG_NAME} ${MICROSERVICE_OPERATION_NAME} ${MICROSERVICE_ANALYSIS_NAME} ${MICROSERVICE_GATEWAY_NAME})

DATABASES=("operationDB" "catalogDB")

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

build_project(){
  # Check for jar dependency (sirio, eurlero)
  setup_dependency
  # Multithread compilation with maven
  ./mvnw clean install --threads 1C -DskipTests
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

show_microservices_log(){

  if [ "$1" == "new" ]; then
    tail_option="--tail 0"
  else
    tail_option=""
  fi

  # geometry != pixels
  cmd="gnome-terminal --geometry=200x50"

  for service in "${MICROSERVICES_TO_LOG[@]}"
  do
      cmd+=" --tab --title='Log of $service' --command='bash -c \"docker compose logs -f $tail_option $service; exec bash\"'"
  done

  eval $cmd
}

check_services(){
    local restarted=0
    for service in "${MICROSERVICES[@]}"; do
        # Check if the microservice is running, otherwise start it
        if ! is_service_running "$service"; then
            echo "Starting service: $service"
            docker compose start "$service"
            restarted=1
        fi
    done
    
    # Recall the function if any service was restarted
    if [ $restarted -eq 1 ]; then
        echo "At least one service was started, checking again..."
        check_services
    fi
}

restart_needed_containers(){

  if is_service_running "$CONFIG_SERVER"; then
    drop_all_mongoDB_databases
    echo "Compose restart of:"
    docker compose restart "${MICROSERVICES[@]}"
    sleep 1 
    check_services
  else
    
    # Starting base containers
    echo "Starting base containers:"
    docker compose up -d "${BASE_SERVICES[@]}"

    echo "Delay (8s) to let config-server start"
    sleep 8
    drop_all_mongoDB_databases
    # Starting microservices
    echo "Starting microservices:"
    docker compose up -d "${MICROSERVICES[@]}"
    sleep 1 
    check_services
  fi
}

connect_to_DBs_GUI(){
  echo "Starting mongoDB-compass GUIs for each DB"
  for database in "${DATABASES[@]}"
  do
      mongodb-compass --file ${COMPASS_CONFIGURATION} "$database" &
  done

  wait
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

usage() {
  echo "Usage: $0 [-b] [-r] [-l]"
  echo "  -b  Build pom of multimodule project"
  echo "  -r  Reload only needed containers"
  echo "  -l  Open log of microservices"
  echo "  -h  This message"
  echo " check script for additional commands"
  exit 1
}

# Options parsing
while getopts "brdl:h" opt; do
  case ${opt} in
    b )
      build_project
      ;;
    r )
      restart_needed_containers
      ;;
    d )
      connect_to_DBs_GUI
      ;;  
    l )
      # Check if there is an additional parameter after -l
      if [[ $OPTARG == "new" ]]; then
        show_microservices_log new
      else
        show_microservices_log
      fi
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