# #!/bin/bash
# set -e

# echo "Building Spring Boot application..."
# mvn clean package -DskipTests

# echo "Starting application..."
# java -jar target/safeguard-0.0.1-SNAPSHOT.jar


#!/bin/bash
set -e

echo "Installing Java and Maven..."

# Detect OS and install accordingly
if command -v apt-get &> /dev/null; then
    # Debian/Ubuntu
    apt-get update
    apt-get install -y openjdk-17-jdk maven
elif command -v yum &> /dev/null; then
    # CentOS/RHEL
    yum install -y java-17-openjdk-devel maven
elif command -v apk &> /dev/null; then
    # Alpine Linux
    apk add --no-cache openjdk17 maven
else
    echo "Unsupported OS. Please use Dockerfile instead."
    exit 1
fi

echo "Java and Maven installed successfully!"

echo "Building Spring Boot application..."
mvn clean package -DskipTests

echo "Starting application..."
java -jar target/safeguard-app.jar
