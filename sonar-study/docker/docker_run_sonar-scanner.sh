#!/usr/bin/env bash

USER_TOKEN=$1
NUMBER_OF_WEEKS=$2
REPOS_DIR=`realpath $3`

docker run -ti -e USER_TOKEN=$USER_TOKEN -e NUMBER_OF_WEEKS=$NUMBER_OF_WEEKS \
    --name sonar-scanner -v "$REPOS_DIR":/root/repos \
    sonar-study:sonar-scanner