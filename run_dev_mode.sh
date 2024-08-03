#!/bin/bash

JAR_FILE="/app/app.jar"
CONFIG_SERVER="config-server"
CONFIG_SERVER_PORT="8888"
SERVICE_NAME="$MICROSERVICE_NAME"

check_config_server() {
    echo "Checkin config server"
    while ! curl -s --head http://$CONFIG_SERVER:$CONFIG_SERVER_PORT/default/$SERVICE_NAME | grep "HTTP/1.1 200" > /dev/null; do
        echo "retry in 1 sec"
        sleep 1
    done
    echo "Config server ok"
}

start_application() {
    java -jar "$JAR_FILE"
    APP_PID=$!
    echo "Applicazione avviata con PID: $APP_PID"
}

# Check confi server is up and running
check_config_server

start_application

