#! /usr/bin/env sh

cd $(dirname $0)
script_location=$PWD

function project {
    project_name=$1
    project_url=$2

    if [ ! -d "../cloned-projects/$project_name" ]; then
	git clone $project_url ../cloned-projects/$project_name
    else
	cd "../cloned-projects/$project_name"
	git pull
	cd $script_location
    fi	
}

project scikit-learn https://github.com/scikit-learn/scikit-learn.git
project theano https://github.com/Theano/Theano.git
project keras https://github.com/keras-team/keras.git
project scikit-image https://github.com/scikit-image/scikit-image
project simplecv https://github.com/sightmachine/SimpleCV
project nltk https://github.com/nltk/nltk
project pattern https://github.com/clips/pattern
project auto-ml https://github.com/ClimbsRocks/auto_ml
project pylearn https://github.com/lisa-lab/pylearn2
project pybrain https://github.com/pybrain/pybrain
project brainstorm https://github.com/IDSIA/brainstorm
project pyevolve https://github.com/perone/Pyevolve
project pytorch https://github.com/pytorch/pytorch
project cogitare https://github.com/cogitare-ai/cogitare
