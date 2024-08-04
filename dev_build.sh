#!/bin/bash


CONFIG_SERVER="config-server"

BASE_SERVICES=("rabbitmq" "mongodb_catalog" "mongodb_operation" "mongodb_analysis" "config-server")

MICROSERVICES=("catalog" "operation" "analysis" "gateway")



build_project(){
  # Multithread compilation with maven
  ./mvnw install --threads C -DskipTests
}

is_service_running() {
  local service=$1
  # Get service' container id
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
  # geometry != pixels
  cmd="gnome-terminal --geometry=150x50"

  for servizio in "${MICROSERVICES[@]}"
  do
      cmd+=" --tab --title='Log of $servizio' --command='bash -c \"docker-compose logs -f $servizio; exec bash\"'"
  done

  eval $cmd
}

restart_needed_containers(){

  if is_service_running "$CONFIG_SERVER"; then
    echo "Compose restart of:"
    docker compose restart "${MICROSERVICES[@]}"
  else
    
    # Starting base containers
    echo "Starting base containers:"
    docker compose up -d "${BASE_SERVICES[@]}"

    echo "Delay (8s) to let config-server start"
    sleep 8

    # Starting microservices
    echo "Starting microservices:"
    docker compose up -d "${MICROSERVICES[@]}"
  fi
}

usage() {
  echo "Usage: $0 [-b] [-r] [-l]"
  echo "  -b  Build pom of multimodule project"
  echo "  -r  Reload only needed containers"
  echo "  -l  Open log of microservices"
  echo "  -h  This message"
  exit 1
}

# Options parsing
while getopts "brlh" opt; do
  case ${opt} in
    b )
      build_project
      ;;
    r )
      restart_needed_containers
      ;;
    l )
      show_microservices_log
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