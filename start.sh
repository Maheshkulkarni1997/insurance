#!/bin/bash
set -e

echo "Building Spring Boot application..."
mvn clean package -DskipTests

echo "Starting application..."
java -jar target/safeguard-0.0.1-SNAPSHOT.jar
