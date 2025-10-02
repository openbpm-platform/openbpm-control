#!/bin/bash

# Default parameters
JAR_NAME="openbpm-control.jar"
PROFILE=${1:-hsqldb}
PORT=8081

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed or not added to PATH"
    exit 1
fi

# Check Java version (minimum 17)
JAVA_VERSION=$(java -version 2>&1 | head -1 | cut -d'"' -f2 | sed 's/^1\.//' | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "Java 17 or higher is required, current version: $JAVA_VERSION"
    exit 1
fi

# Function to open the browser
open_browser() {
    sleep 20 # Give the application time to start

    URL="http://localhost:$PORT"
    echo "Attempting to open: $URL"

    # For different OS
    case "$(uname -s)" in
        Linux*)  xdg-open "$URL" ;;
        Darwin*) open "$URL" ;;
        *)       echo "Auto-opening is not supported for this OS" ;;
    esac
}

# JVM parameters
JVM_OPTS="-Xms256m -Xmx512m -Dspring.profiles.active=$PROFILE"

# Start the application
echo "Starting the application with profile: $PROFILE..."
java $JVM_OPTS -jar $JAR_NAME $SPRING_OPTS "$@" --server.port=$PORT 2>&1 | tee /tmp/openbpm-control.log &

# Wait for the "Tomcat started on port" message
while ! grep -q "Tomcat started on port" /tmp/openbpm-control.log; do
    sleep 1
done

# Open the browser after the application has started
open_browser

# Wait for the application process to finish
wait $!