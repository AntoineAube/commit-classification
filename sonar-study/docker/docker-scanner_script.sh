#!/bin/bash

# Example: ./start-analyses.sh scikit-learn:scikit-learn ~/workspace/scikit-learn/ 10

SONARQUBE_URL=http://172.17.0.1:9000
# PROJECT_ID=$1
# PROJECT_ROOT=$2
# NUMBER_OF_WEEKS=$3

# checkout the git in the successive commits extracted before, and start sonarqube analyses
# $1 is the branch name, $2 is the file containing commits, $3 is the repo path
function start-sonarqube-analyses 
{
    cd $3
    git checkout $1 &> /dev/null
    echo -n "$(basename $3): SonarQube analyses ongoing... 0/$NUMBER_OF_WEEKS done."

    numDone=0
    while read line; do
        line=( $line )
        checksum=${line[0]}
        date=`date -d @${line[1]} +%Y-%m-%d`

        git checkout $checksum 2> /dev/null

        sonar-scanner \
        -Dsonar.projectKey=`basename $3` \
        -Dsonar.sources=. \
        -Dsonar.host.url=$SONARQUBE_URL \
        -Dsonar.login=$USER_TOKEN \
        -Dsonar.projectDate=$date &> /dev/null

        if (( $? != 0 ));then
            # echo "Error during sonar-scanner call. Logged in $LOG_FILE."
            echo "Error during sonar-scanner call"
            exit 1
        fi

        numDone=$(( $numDone + 1 ))
        echo -ne "\r$(basename $3): SonarQube analyses ongoing... $numDone/$NUMBER_OF_WEEKS done."
    done < $2
    echo ""

    git checkout $1 &> /dev/null
}


for repo in /root/data/*; do
    cd "$repo"
    branch=`cat $repo/branch`
    repo_name=`basename $repo`
    start-sonarqube-analyses "$branch" "$repo/git.commits" "/root/repos/$repo_name"
done
