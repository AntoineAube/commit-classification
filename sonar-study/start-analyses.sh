#!/usr/bin/env bash

# Example: ./start-analyses.sh scikit-learn:scikit-learn ~/workspace/scikit-learn/ 10

REPOS_DIR=$1
NUMBER_OF_WEEKS=2

cd `realpath $(dirname $0)`

./create-sonarscanner-data.sh "$REPOS_DIR" docker/data $NUMBER_OF_WEEKS &> /dev/null

DOCKER_DATA="$(dirname $0)/docker/data"
mkdir -p "$DOCKER_DATA"

docker pull sonarqube:7.0
docker pull openjdk:8
echo "Building sonar-scanner docker image..."
docker/docker_build_sonar-scanner.sh &> /dev/null

docker/docker_stop.sh &> /dev/null
docker/docker_run_sonarqube.sh &> /dev/null

sonar_started=-1

echo "Starting sonarqube docker container..."

while [ $sonar_started -ne 0 ]; do
    curl -m 1 -I http://127.0.0.1:9000  &> /dev/null
    sonar_started=$?
    #echo $sonar_started
    sleep 1
done

echo -n "Please press enter when the webpage indicates sonarqube has done loading."
xdg-open "http://127.0.0.1:9000/"
read

echo "Please login with user:password \"admin:admin\" and create a user token as prompted."
echo -n "User token: "
xdg-open "http://127.0.0.1:9000/sessions/new"
read USER_TOKEN
echo $USER_TOKEN > "$DOCKER_DATA/user_token"

docker/docker_run_sonar-scanner.sh $USER_TOKEN $NUMBER_OF_WEEKS "$REPOS_DIR"