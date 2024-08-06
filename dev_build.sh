#!/bin/bash


CONFIG_SERVER="config-server"

BASE_SERVICES=("rabbitmq" "mongodb_catalog" "mongodb_operation" "mongodb_analysis" "config-server")

MICROSERVICES=("catalog" "operation" "analysis" "gateway" "mongodb_catalog")
MICROSERVICES_TO_LOG=("catalog" "operation" "analysis" "gateway")

DATABASES=("analysisDB" "operationDB" "catalogDB")

# Funzione per installare un JAR nel repository Maven locale
install_jar() {
    local group_id="$1"
    local artifact_id="$2"
    local version="$3"
    local jar_path="$4"
    
    # Costruisci il percorso del jar nel repository locale di Maven
    local local_repo_path="$HOME/.m2/repository/$(echo $group_id | tr '.' '/')/$artifact_id/$version/$artifact_id-$version.jar"

    # Controlla se il jar esiste già nel repository locale
    if [ -f "$local_repo_path" ]; then
        echo "Il jar di $artifact_id è già installato nel repository Maven locale."
    else
        echo "Il jar di $artifact_id non è presente nel repository Maven locale. Procedo con l'installazione..."
        
        # Verifica che il file jar esista nella cartella lib
        if [ -f "$jar_path" ]; then
            # Installa il jar nel repository locale
            mvn install:install-file -Dfile="$jar_path" -DgroupId="$group_id" -DartifactId="$artifact_id" -Dversion="$version" -Dpackaging=jar
            
            if [ $? -eq 0 ]; then
                echo "Il jar di $artifact_id è stato installato con successo nel repository Maven locale."
            else
                echo "Si è verificato un errore durante l'installazione del jar di $artifact_id."
            fi
        else
            echo "Errore: Il file $jar_path non esiste."
            exit 1
        fi
    fi
}

setup_dependency(){
  install_jar "com.eulero" "eulero" "1.0" "./qesm_src/lib/eulero.jar"
  install_jar "com.sirio" "sirio" "1.0" "./qesm_src/lib/sirio.jar"
}

build_project(){
  # Check for jar dependency (sirio, eurlero)
  setup_dependency
  # Multithread compilation with maven
  ./mvnw install --threads 1C -DskipTests
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

connect_to_DBs_GUI(){
  echo "Starting mongoDB-compass GUIs for each DB"
  for database in "${DATABASES[@]}"
  do
      mongodb-compass --file "compass-connections.json" "$database" &
  done

  wait
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
      # Controlla se c'è un parametro aggiuntivo dopo -l
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