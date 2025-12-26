@echo off
rem 切换到脚本所在目录
pushd "%~dp0" || exit /b 1

rem 设置 JAVA_HOME 指向当前目录下的 jdk 子目录
set "JAVA_HOME=%~dp0jdk"

rem 以 lib\* 作为类路径运行主类
"%JAVA_HOME%\bin\java" -cp ".\lib\*" rip.sayori.rmcr.Launcher

popd