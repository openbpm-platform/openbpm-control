@echo off
setlocal enabledelayedexpansion

:: Check if Java is installed
where java >nul 2>nul
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not added to PATH
    exit /b 1
)

:: Check Java version
for /f "tokens=3 delims=." %%a in ('java -version 2^>^&1 ^| findstr /i "version"') do (
    set JAVA_VERSION=%%a
)
set JAVA_VERSION=!JAVA_VERSION:"=!
for /f "tokens=1 delims=." %%a in ("!JAVA_VERSION!") do (
    set JAVA_MAJOR=%%a
)
if !JAVA_MAJOR! lss 17 (
    echo Java 17 or higher is required, current version: !JAVA_VERSION!
    exit /b 1
)

:: Default parameters
set JAR_NAME=openbpm-control.jar
set PORT=8081
set PROFILE=%1
if "!PROFILE!"=="" (
    set PROFILE=hsqldb
)

:: JVM parameters
set JVM_OPTS=-Xms256m -Xmx512m -Dspring.profiles.active=!PROFILE!

:: Start the application
echo Starting the application with profile: !PROFILE!...

:: Redirect output to a temporary file to monitor logs
set LOG_FILE=%TEMP%\openbpm-control.log
java %JVM_OPTS% -jar %JAR_NAME% %SPRING_OPTS% --server.port=%PORT% %* > "%LOG_FILE%" 2>&1 &

:: Wait for the "Tomcat started on port" message
:WAIT_FOR_TOMCAT
timeout /t 1 /nobreak >nul
findstr /c:"Tomcat started on port" "%LOG_FILE%" >nul
if %errorlevel% neq 0 goto WAIT_FOR_TOMCAT

:: Open the browser after the application has started
start "" "http://localhost:%PORT%"