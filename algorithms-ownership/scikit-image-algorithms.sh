#! /usr/bin/env sh

cd $(dirname $0)

script_location=$PWD
output_location=$script_location/drilled-informations/scikit-image-algorithms.csv

echo "ALGORITHM" > $output_location

cd ../cloned-projects/scikit-image

for filename in `find ./skimage -type f \( -name "*.py" -or -name "*.pyx" \) -not -path "*test_*" -not -path "*/_*" -not -path "*setup.py" -not -path "*util.py" -not -path "*/util/*" -not -path "*./utils/*" -not -path "*/scripts/*"`
do
    echo $filename >> $output_location
done
