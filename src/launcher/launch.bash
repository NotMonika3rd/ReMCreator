#!/usr/bin/env bash
cd "$(dirname "$0")" || exit
JAVA_HOME=$(pwd)/jdk
"$JAVA_HOME"/bin/java --class-path "./lib/*" "rip.sayori.rmcr.Launcher"