#!/usr/bin/env bash

$GRADLE_HOME/bin/gradle build --warning-mode=none && java -jar build/libs/paul-the-octopus-0.1.jar $1 $2 $3 $4 $5 $6 $7 $8