@echo off
setlocal enabledelayedexpansion

:: Set the JAR file name
set JAR_NAME=openbpm-control.jar
set PROCESS_PID=

:: Find the process ID of the running application
for /f "tokens=2 delims=," %%a in ('tasklist /nh /fo csv /fi "IMAGENAME eq java.exe" ^| find "%JAR_NAME%"') do (
    set PROCESS_PID=%%~a
)

:: Check if the application is running
if "!PROCESS_PID!"=="" (
    echo Application is not running
    exit /b 0
)

:: Stop the application
echo Stopping the application (PID: !PROCESS_PID!)...
taskkill /F /PID !PROCESS_PID!

:: Wait until the process is completely terminated
:WAIT_FOR_TERMINATION
timeout /t 1 /nobreak >nul
tasklist /fi "PID eq !PROCESS_PID!" | find "!PROCESS_PID!" >nul
if %errorlevel%==0 goto WAIT_FOR_TERMINATION

:: Display success message after the process is terminated
echo Application successfully stopped