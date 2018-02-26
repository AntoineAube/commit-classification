#! /usr/bin/env sh

cd $(dirname $0)


echo "[DATA] Clone/Update the repositories."
./scripts/update-repositories.sh


echo "[STUDY] Run 'researchers-contributors' part."
./researchers-contributors/study.sh

echo "[STUDY] Run 'algorithms-ownership' part."
./algorithms-ownership/study.sh
