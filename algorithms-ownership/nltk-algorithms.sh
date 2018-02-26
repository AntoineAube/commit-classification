#! /usr/bin/env sh

cd $(dirname $0)

script_location=$PWD
output_location=$script_location/drilled-informations/nltk-algorithms.csv

echo "ALGORITHM" > $output_location

cd ../cloned-projects/nltk

for filename in `find ./nltk -type f \( -name "*.py" -or -name "*.pyx" \) -not -path "./nltk/test/*" -not -path "*/_*" -not -path "*api.py" -not -path "*setup.py" -not -path "*util.py"`
do
    if [ -e $(dirname $filename)/api.py ]
    then
	echo $filename >> $output_location
    fi
done
