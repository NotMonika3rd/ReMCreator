@echo off
pushd "%~dp0" || exit /b 1
set "JAVA_HOME=%~dp0jdk"
"%JAVA_HOME%\bin\java" -cp ".\lib\*" rip.sayori.rmcr.Launcher
popd