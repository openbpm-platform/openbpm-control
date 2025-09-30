@echo off
setlocal enabledelayedexpansion

:: Check if Java is installed
where java >nul 2>nul
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not added to PATH
    exit /b 1
)

:: Check Java version
for /f "tokens=3" %%a in ('java -version 2^>^&1 ^| findstr /i "version"') do (
    set JAVA_VERSION=%%a
)
set JAVA_VERSION=!JAVA_VERSION:"=!
set JAVA_MAJOR=!JAVA_VERSION:~2,2!
if !JAVA_MAJOR! lss 17 (
    echo Java 17 or higher is required, current version: !JAVA_VERSION!
    exit /b 1
)

:: Default parameters
set JAR_NAME=openbpm-control.jar
set DB_NAME=control
set DB_PATH=.\data\%DB_NAME%
set DB_USER=sa
set DB_PASS=
set PORT=8081

:: JVM parameters
set JVM_OPTS=-Xms256m -Xmx512m

:: Spring Boot parameters
set SPRING_OPTS=--spring.datasource.url=jdbc:hsqldb:file:%DB_PATH% --spring.datasource.username=%DB_USER% --spring.datasource.password=%DB_PASS%

:: Start the application
echo Starting the application with HSQLDB...
echo Database path: %DB_PATH%

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