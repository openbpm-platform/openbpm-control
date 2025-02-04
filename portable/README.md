# OpenBPM Control with HSQLDB

This guide explains how to run the OpenBPM Control application using the embedded HSQLDB.

## Requirements

- **Java 17** or higher
- For Windows: Command Prompt (CMD/PowerShell)
- For Linux/macOS: Terminal

## Project Structure

```
openbpm-control/
├── openbpm-control.jar # Compiled JAR file of the application
├── run.sh # Launch script for Linux/macOS
├── run.bat # Launch script for Windows
├── stop.sh # Stop script for Linux/macOS
├── stop.bat # Stop script for Windows
└──.jmix/ # Folder for storing HSQLDB data (will be created automatically)
```

## Quick Start

### 1. Preparation

- Unzip the archive and place all files in the desired directory
- Ensure that the scripts `run.sh` (for Linux/macOS) and `run.bat` (for Windows) are in the same folder

### 2. Launching the Application

**For Linux/macOS:**
```bash
# Grant execute permissions to the script
chmod +x run.sh

# Launch with default settings
./run.sh
```

**Для Windows:**

```
# Simply execute in the command prompt
run.bat
```

### 3. Checking work

After a successful launch:

1. Open in your browser: http://localhost:8081
2. Log in using the username and password `admin/admin`

