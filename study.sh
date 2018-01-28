#! /usr/bin/env sh

echo "[CLEAN] Removing all produced files."

rm -f datasets-builder.jar *.html


echo "[DEPENDENCIES] Fetching the PIP dependencies for the study."
sudo pip install -r requirements.txt


echo "[REPOSITORIES] Fetching or update studied projects repositories."

if [ ! -d scikit-learn/ ]; then
    git clone https://github.com/scikit-learn/scikit-learn.git scikit-learn
else
    echo "[REPOSITORIES] 'scikit-learn' is already cloned."
fi

if [ ! -d keras/ ]; then
    git clone https://github.com/keras-team/keras.git keras
else
    echo "[REPOSITORIES] 'keras' is already cloned."
fi

if [ ! -d theano/ ]; then
    git clone https://github.com/Theano/Theano.git theano
else
    echo "[REPOSITORIES] 'theano' is already cloned."
fi


echo "[DIGGER] Compiling the repositories digger."
mvn -f datasets-builder/pom.xml clean package assembly:single
echo "[DIGGER] Moving the digger to the root of the repository."
mv datasets-builder/target/datasets-builder-1.0-SNAPSHOT-jar-with-dependencies.jar ./datasets-builder.jar

echo "[DIGGER] Digging 'scikit-learn'..."
java -jar ./datasets-builder.jar -r scikit-learn/ -o study-results/scikit-learn
echo "[DIGGER] Digging 'keras'..."
java -jar ./datasets-builder.jar -r keras/ -o study-results/keras
echo "[DIGGER] Digging 'theano'..."
java -jar ./datasets-builder.jar -r theano/ -o study-results/theano
