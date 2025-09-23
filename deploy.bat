@echo off
setlocal

set "TOMCAT_HOME=C:\apache-tomcat-10.1.28"
set "TOMCAT_WEBAPPS=%TOMCAT_HOME%\webapps"
set WAR_FILE=target\SpringInit.war

echo ------------------------------
echo Compiling and packaging Maven...
echo ------------------------------
call mvn clean package

echo mvn done, return : %ERRORLEVEL%

if errorlevel 1 (
    echo ERREUR : Maven compilation failed
    pause
    exit /b 1
)

if not exist "%WAR_FILE%" (
    echo ERREUR : WAR file not found : %WAR_FILE%
    pause
    exit /b 1
)

echo ------------------------------
echo Copying war file to Tomcat ...
echo ------------------------------

copy /Y "%WAR_FILE%" "%TOMCAT_WEBAPPS%\"

if errorlevel 1 (
    echo ERREUR : La copie du WAR vers Tomcat a échoué.
    pause
    exit /b 1
)

echo Deployment done successfully.

echo Starting Tomcat...
set CATALINA_HOME=%TOMCAT_HOME%
call "%TOMCAT_HOME%\bin\catalina.bat" start
if %errorlevel% neq 0 (
    echo Error: Failed to start Tomcat.
    exit /b 1
)

pause
