#!/bin/bash

docker-compose up -d

echo "Waiting for MySQL to be ready..."
sleep 20

java -jar ./dist/com.example-1.0-SNAPSHOT.jar