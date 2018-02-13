#!/usr/bin/env bash

# Example: ./start-analyses.sh scikit-learn:scikit-learn ~/workspace/scikit-learn/ git-checkout-numbers.txt

PROJECT_ID=$1
PROJECT_ROOT=$2
CHECKOUTS_FILE=$3

function git-loop 
{
while read line; do
    line=( $line )
    checksum=${line[0]}
    date=${line[1]}
    
    cd $PROJECT_ROOT
    git checkout $checksum

    sonar-scanner \
    -Dsonar.projectKey=scikit-learn:scikit-learn \
    -Dsonar.sources=. \
    -Dsonar.host.url=http://localhost:9000 \
    -Dsonar.login=8f1c8af97c56c4d022c5ad2b03d9022cea1a8832 \
    -Dsonar.projectDate=$date
done
}

git-loop < $CHECKOUTS_FILE