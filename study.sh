#! /usr/bin/env sh

echo "[DATA] Clone/Update the repositories."
./scripts/update-repositories.sh

echo "[STUDY] Run 'researchers-contributors' part."
./researchers-contributors/study.sh
