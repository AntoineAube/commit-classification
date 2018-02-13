#!/usr/bin/env bash

# Example: ./start-analyses.sh scikit-learn:scikit-learn ~/workspace/scikit-learn/ 10

if [ "$#" -ne 3 ]; then
    echo "Usage: ./start-analyses.sh PROJECT_ID PROJECT_ROOT NUMBER_OF_WEEKS"
    exit 1
fi

PROJECT_ID=$1
PROJECT_ROOT=$2
NUMBER_OF_WEEKS=$3
SECONDS_PER_WEEK=604800
LOG_FILE=`readlink -f $PROJECT_ROOT`/sonarqube_analyses.log

# create a list of commit checksums which are at least 1 week from one another, going as far as asked (NUMBER_OF_WEEKS)
function extract-checksums
{
    echo -n "Selecting at most $NUMBER_OF_WEEKS commits..."

    cd $PROJECT_ROOT
    git --no-pager log -99999 --pretty=format:'%H %ct' > git.commits.tmp
    
    read line < git.commits.tmp
    line=( $line )
    checksum=${line[0]}
    date=${line[1]}
    
    maxNextTimestamp=$(( $date - $SECONDS_PER_WEEK ))
    weekNumber=1
    echo $checksum $date> git.commits

    while read line; do
        if (( $weekNumber >= $NUMBER_OF_WEEKS ));then
            break
        fi
        
        line=( $line )
        checksum=${line[0]}
        date=${line[1]}

        if (( $date <= $maxNextTimestamp ));then
            maxNextTimestamp=$(( $maxNextTimestamp - $SECONDS_PER_WEEK ))
            weekNumber=$(( $weekNumber + 1 ))
            # insert checksum at the beginning of file, so we have commits checksum in growing chronological order
            sed -i "1s/^/$checksum $date\n/" git.commits
        fi
    done < git.commits.tmp

    NUMBER_OF_WEEKS=$weekNumber
    rm git.commits.tmp
    echo -e "\rSelecting at most $NUMBER_OF_WEEKS commits... Done."
    echo "Selected $weekNumber commits."
}

# checkout the git in the successive commits extracted before, and start sonarqube analyses
function start-sonarqube-analyses 
{
    cd $PROJECT_ROOT
    echo "" > $LOG_FILE

    echo -n "SonarQube analyses ongoing... 0/$NUMBER_OF_WEEKS done."

    numDone=0
    while read line; do
        line=( $line )
        checksum=${line[0]}
        date=`date -d @${line[1]} +%Y-%m-%d`

        git checkout $checksum 2> /dev/null

        sonar-scanner \
        -Dsonar.projectKey=scikit-learn:scikit-learn \
        -Dsonar.sources=. \
        -Dsonar.host.url=http://localhost:9000 \
        -Dsonar.login=51e2c19ff1fd14320f223e68afb5c2dac6a79811 \
        -Dsonar.projectDate=$date > $LOG_FILE

        if (( $? != 0 ));then
            echo "Error during sonar-scanner call. Logged in $LOG_FILE."
            exit 1
        fi

        numDone=$(( $numDone + 1 ))
        echo -ne "\rSonarQube analyses ongoing... $numDone/$NUMBER_OF_WEEKS done."
    done < git.commits
    echo ""

    rm git.commits $LOG_FILE
}

extract-checksums
start-sonarqube-analyses