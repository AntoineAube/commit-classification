#! /usr/bin/env sh

cd $(dirname $0)
script_location=$PWD

echo "[DRILL] Preparing drilled artefacts directory."
rm -rf drilled-informations
mkdir drilled-informations

function ownership {
    project=$1

    echo "[DRILL] Drilling ownership in project '$project'."
    
    cd ../cloned-projects/$project

    $script_location/git-blamer.sh $script_location/drilled-informations/$project-ownership.csv

    cd $script_location
}

echo "[DRILL] Begin project drilling."
ownership scikit-learn
ownership scikit-image
ownership nltk


echo "[DRILL] Drilling algorithms in project 'scikit-learn'"
./scikit-learn-algorithms.sh

echo "[DRILL] Drilling algorithms in project 'scikit-image'"
./scikit-image-algorithms.sh

echo "[DRILL] Drilling algorithms in project 'nltk'"
./nltk-algorithms.sh
