#! /usr/bin/env sh

cd $(dirname $0)
script_location=$PWD

function project {
    project_name=$1
    project_url=$2

    echo "Project '$project_name'..."

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
project featureforge https://github.com/machinalis/featureforge
project metric-learn https://github.com/metric-learn/metric-learn
project simpleai https://github.com/metric-learn/metric-learn
project astroML https://github.com/astroML/astroML
project Lasagne https://github.com/Lasagne/Lasagne
project hebel https://github.com/hannes-brt/hebel
project Chainer https://github.com/chainer/chainer
project prophet https://github.com/facebook/prophet
project gensim https://github.com/RaRe-Technologies/gensim
project Surprise https://github.com/NicolasHug/Surprise
project crab https://github.com/muricoca/crab
project nilearn https://github.com/nilearn/nilearn
project pyhsmm https://github.com/mattjj/pyhsmm
project skll https://github.com/EducationalTestingService/skll
project spearmint https://github.com/JasperSnoek/spearmint
project deap https://github.com/deap/deap
project mlxtend https://github.com/rasbt/mlxtend
project tpot https://github.com/EpistasisLab/tpot
project pgmpy https://github.com/pgmpy/pgmpy
project milk https://github.com/luispedro/milk

