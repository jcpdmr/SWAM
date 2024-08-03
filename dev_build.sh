#!/bin/bash

# Esegui la compilazione del progetto Maven
./mvnw install --threads 4 -DskipTests

# Nome del container da controllare
CONFIG_SERVER="config-server"

# Nomi dei servizi da avviare inizialmente
BASE_SERVICES=("rabbitmq" "mongodb_catalog" "mongodb_operation" "mongodb_analysis" "config-server")

# Nomi dei servizi da avviare dopo lo sleep
MICROSERVICES=("catalog" "operation" "analysis" "gateway")


is_service_running() {
  local service=$1
  # Ottieni l'ID del container per il servizio
  local container_id=$(docker compose ps -q "$service")
  
  # Verifica se il container esiste e se è in esecuzione
  if [ -n "$container_id" ]; then
    local status=$(docker inspect -f '{{.State.Status}}' "$container_id")
    [ "$status" == "running" ]
  else
    return 1
  fi
}

if is_service_running "$CONFIG_SERVER"; then
  echo "Avvio il compose restart solo dei microservizi"
  docker compose restart "${MICROSERVICES[@]}"
else
  echo "Avvio il compose up"

  # Avvia Docker Compose con i servizi iniziali
  echo "Avviando i servizi iniziali:"
  docker compose up -d "${BASE_SERVICES[@]}"

  # Attendi 5 secondi
  echo "Delay di 8 secondi per dare tempo al config-server di avviarsi"
  sleep 8

  # Avvia i servizi rimanenti
  echo "Avviando gli altri microservizi:"
  docker compose up -d "${MICROSERVICES[@]}"
fi


# Costruisci il comando per gnome-terminal
cmd="gnome-terminal --geometry=150x50"

for servizio in "${MICROSERVICES[@]}"
do
    cmd+=" --tab --title='Log di $servizio' --command='bash -c \"docker-compose logs -f $servizio; exec bash\"'"
done

# Esegui il comando
eval $cmd

exit 0