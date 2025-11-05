#!/bin/bash
cd safeguard
mvn clean package
java -jar target/your-app-name.jar  # Adjust the jar name
