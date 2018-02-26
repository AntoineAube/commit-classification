#! /usr/bin/env sh

cd $(dirname $0)

script_location=$PWD
output_location=$script_location/drilled-informations/scikit-learn-algorithms.csv

echo "ALGORITHM" > $output_location

cd ../cloned-projects/scikit-learn

for filename in `find ./sklearn -type f \( -name "*.py" -or -name "*.pyx" \)`
do
    if grep -q "def fit(self" "$filename"
    then
	echo $filename >> $output_location
    fi
done
