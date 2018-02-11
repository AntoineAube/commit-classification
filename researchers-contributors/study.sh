#! /usr/bin/env sh

cd $(dirname $0)

echo "[CLEAN] Remove the artefact of the repositories driller."
rm -f repositories-driller.jar

echo "[CLEAN] Remove the HTML output."
rm -f *.html

echo "[DEPENDENCIES] Install missing dependencies from PIP."
sudo pip install -r requirements.txt

echo "[BUILD] Build the artefact of the repositories driller."
mvn -f repositories-driller/pom.xml clean package assembly:single

echo "[BUILD] Move the artefact of the repositories driller to the current folder."
mv repositories-driller/target/repositories-driller-jar-with-dependencies.jar ./repositories-driller.jar

echo "[EXECUTION] Drill the repositories."
java -jar ./repositories-driller.jar -r ../cloned-projects -o drilled-informations

echo "[EXECUTION] Preprocess the commits: metrics to determine which contributor is a researcher."
./find-researchers.py -c drilled-informations -s known-contributors.csv

echo "[CLEAN] Remove the artefact of the repositories driller."
rm -f repositories-driller.jar
