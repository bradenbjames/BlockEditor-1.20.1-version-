@echo off
rem Gradle wrapper for Windows (fixed)
setlocal
rem Use JAVA_HOME if set, otherwise expect java on PATH
if defined JAVA_HOME (
  set "JAVA_EXE=%JAVA_HOME%\bin\java.exe"
) else (
  set "JAVA_EXE=java"
)

set "DIR=%~dp0"
set "WRAPPER_JAR=%DIR%gradle\wrapper\gradle-wrapper.jar"

if not exist "%WRAPPER_JAR%" (
  echo Could not find the Gradle wrapper jar at "%WRAPPER_JAR%".
  exit /b 1
)

"%JAVA_EXE%" -jar "%WRAPPER_JAR%" %*

endlocal
