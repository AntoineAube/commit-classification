#!/usr/bin/env bash

docker stop sonarqube
docker rm sonarqube
docker stop sonar-scanner
docker rm sonar-scanner