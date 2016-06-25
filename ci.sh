#!/bin/bash
# Run it from root project directory!

# This command does the following:
# - Builds the project
# - Runs Android Lint
# - Runs Unit Tests under JVM
# - Packages the APK
./gradlew clean build test -PdisablePreDex