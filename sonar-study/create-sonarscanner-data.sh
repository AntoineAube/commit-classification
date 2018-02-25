#!/usr/bin/env bash

REPOS_DIR=`realpath "$1"`
OUTPUT_DIR=$2
NUMBER_OF_WEEKS=$3

OUTPUT_DIR=`realpath "$OUTPUT_DIR"`

SECONDS_PER_WEEK=604800

# create a list of commit checksums which are at least 1 week from one another, going as far as asked (NUMBER_OF_WEEKS)
# $1 is repo root, $2 is output dir
function extract-checksums
{
    echo -n "Selecting at most $NUMBER_OF_WEEKS commits..."

    cd $1
    git --no-pager log -99999 --pretty=format:'%H %ct' > git.commits.tmp
    
    read line < git.commits.tmp
    line=( $line )
    checksum=${line[0]}
    date=${line[1]}
    
    maxNextTimestamp=$(( $date - $SECONDS_PER_WEEK ))
    weekNumber=1
    echo $checksum $date> "$2/git.commits"

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
            sed -i "1s/^/$checksum $date\n/" "$2/git.commits"
        fi
    done < git.commits.tmp

    echo -e "\rSelecting at most $NUMBER_OF_WEEKS commits... Done."
    NUMBER_OF_WEEKS=$weekNumber
    rm git.commits.tmp
    echo "Selected $weekNumber commits."
}

cd $REPOS_DIR
for repo in *; do
    mkdir -p "$OUTPUT_DIR/$repo"
    cd "$repo"
    git status | head -n 1 | cut -d " " -f 3 > "$OUTPUT_DIR/$repo"/branch
    extract-checksums "$REPOS_DIR/$repo" "$OUTPUT_DIR/$repo"

    cd $REPOS_DIR
done