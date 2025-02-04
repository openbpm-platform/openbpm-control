#!/bin/bash

JAR_NAME="openbpm-control.jar"
PROCESS_PID=$(ps aux | grep "$JAR_NAME" | grep -v grep | awk '{print $2}')

# Check if the application is running
if [ -z "$PROCESS_PID" ]; then
    echo "Application is not running"
    exit 0
fi

# Stop the application
echo "Stopping the application (PID: $PROCESS_PID)..."
kill -9 $PROCESS_PID

# Wait until the process is completely terminated
while kill -0 $PROCESS_PID 2>/dev/null; do
    sleep 1
done

# Display success message after the process is terminated
echo "Application successfully stopped"