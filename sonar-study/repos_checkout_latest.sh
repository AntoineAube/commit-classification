#!/usr/bin/env bash

REPOS_DIR=`realpath $1`
# cd $REPOS_DIR

for repo in $REPOS_DIR/*; do
    cd $repo
    branch=$(git status | head -n 1 | cut -d " " -f 3)
    git checkout $branch
done