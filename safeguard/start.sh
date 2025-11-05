#!/bin/bash
cd safeguard
mvn clean package
java -jar target/safeguard-0.0.1-SNAPSHOT.jar  # Adjust the jar name
