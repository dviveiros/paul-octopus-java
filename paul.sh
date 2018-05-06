#!/usr/bin/env bash

gradle build --warning-mode=none && java -jar build/libs/paul-the-octopus-0.1.jar $1 $2 $3