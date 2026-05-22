@echo off
setlocal

set DIR=%~dp0
set WRAPPER_JAR=%DIR%gradle\wrapper\gradle-wrapper.jar
set GRADLE_CLI_JAR=%DIR%gradle\wrapper\gradle-cli.jar
set PROPS_FILE=%DIR%gradle\wrapper\gradle-wrapper.properties
if not defined GRADLE_USER_HOME set GRADLE_USER_HOME=%DIR%.gradle

if not exist "%WRAPPER_JAR%" (
  echo Missing %WRAPPER_JAR%
  echo Generate the Gradle Wrapper from Android Studio:
  echo   Gradle tool window -^> (gear) -^> "Generate Gradle Wrapper"
  echo Or run (using a system Gradle install):
  echo   gradle wrapper --gradle-version 8.6
  exit /b 1
)

if defined JAVA_HOME (
  set JAVA_EXE=%JAVA_HOME%\bin\java.exe
) else (
  set JAVA_EXE=java.exe
)

"%JAVA_EXE%" -cp "%WRAPPER_JAR%;%GRADLE_CLI_JAR%" org.gradle.wrapper.GradleWrapperMain %*
endlocal
