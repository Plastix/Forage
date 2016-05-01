#
#           Circle CI & gradle.properties live in harmony
# 
# Android convention is to store your API keys in a local, non-versioned
# gradle.properties file. Circle CI doesn't allow users to upload pre-populated
# gradle.properties files to store this secret information, but instaed allows
# users to store such information as environment variables.
#
# This script creates a local gradle.properties file on current the Circle CI
# instance. It then reads environment variables which a user
# has defined in their Circle CI project settings environment variables, and 
# writes this value to the Circle CI instance's gradle.properties file.
# 
# You must execute this script via your circle.yml as a pre-process dependency,
# so your gradle build process has access to all variables.
#
#   dependencies:
#       pre:
#        - source environmentSetup.sh && copyEnvVarsToGradleProperties
#
# Adapted from https://gist.github.com/KioKrofovitch/716e6a681acb33859d16

#!/usr/bin/env bash

function copyEnvVarsToGradleProperties {
    GRADLE_PROPERTIES=$HOME"/.gradle/gradle.properties"
    export GRADLE_PROPERTIES
    echo "Gradle Properties should exist at $GRADLE_PROPERTIES"

    if [ ! -f "$GRADLE_PROPERTIES" ]; then
        echo "Gradle Properties does not exist"

        echo "Creating Gradle Properties file..."
        touch $GRADLE_PROPERTIES

        echo "Writing API keys to gradle.properties..."
        echo "OKAPI_US_CONSUMER_KEY=$OKAPI_US_CONSUMER_KEY" >> $GRADLE_PROPERTIES
        echo "OKAPI_US_CONSUMER_SECRET=$OKAPI_US_CONSUMER_SECRET" >> $GRADLE_PROPERTIES
        echo "GOOGLE_MAPS_API_KEY=$GOOGLE_MAPS_API_KEY" >> $GRADLE_PROPERTIES
    fi
}