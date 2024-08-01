#!/bin/bash

# Add all necessary setup

# Build config-server .jar
./config-server/mvnw -f ./config-server package 

# Build qesm_src .jar
mvn -f ./qesm_src package -DskipTests