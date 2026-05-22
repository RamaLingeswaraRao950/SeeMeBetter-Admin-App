#!/usr/bin/env sh
set -e

DIR="$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)"

WRAPPER_JAR="$DIR/gradle/wrapper/gradle-wrapper.jar"
GRADLE_CLI_JAR="$DIR/gradle/wrapper/gradle-cli.jar"
PROPS_FILE="$DIR/gradle/wrapper/gradle-wrapper.properties"

export GRADLE_USER_HOME="${GRADLE_USER_HOME:-$DIR/.gradle}"

if [ ! -f "$WRAPPER_JAR" ]; then
  echo "Missing $WRAPPER_JAR"
  echo "Generate the Gradle Wrapper from Android Studio:"
  echo "  Gradle tool window -> (gear) -> 'Generate Gradle Wrapper'"
  echo "Or run (using a system Gradle install):"
  echo "  gradle wrapper --gradle-version 8.6"
  exit 1
fi

JAVA_EXEC="${JAVA_HOME:+$JAVA_HOME/bin/}java"
if [ ! -f "$GRADLE_CLI_JAR" ]; then
  echo "Missing $GRADLE_CLI_JAR"
  echo "Recreate the Gradle Wrapper from Android Studio:"
  echo "  Gradle tool window -> (gear) -> 'Generate Gradle Wrapper'"
  exit 1
fi

exec "$JAVA_EXEC" -cp "$WRAPPER_JAR:$GRADLE_CLI_JAR" org.gradle.wrapper.GradleWrapperMain "$@"
