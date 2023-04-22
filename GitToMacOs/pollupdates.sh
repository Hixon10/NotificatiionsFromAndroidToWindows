#!/bin/bash
set -euo pipefail

GITHUB_LOGIN="";
GITHUB_PERSONAL_TOKEN="";
GITHUB_PULL_URL="";

GIT_DIRECOTRY="/notifications";
GIT_EVENTS_FILE_PATH="${GIT_DIRECOTRY}/events.txt";

cd $GIT_DIRECOTRY;

PULL_RESULT=$(git pull $GITHUB_PULL_URL);

if [[ ${PULL_RESULT} != *"Already up to date"* ]];then
    echo "not contains";
else
    echo "already updated; finished the script"; 
    exit 0;
fi

APP_NAME=$(cut -d' ' -f1 $GIT_EVENTS_FILE_PATH);

if [[ ${APP_NAME} == *"telegram"* ]];then
    echo "ignore telegram notifications";
    exit 0;
fi

echo "Showing notification";

EVENT_TIMESTAMP_IN_MS=$(cut -d' ' -f2 $GIT_EVENTS_FILE_PATH);
EVENT_TIMESTAMP=$(expr $EVENT_TIMESTAMP_IN_MS / 1000);
EVENT_DATE=$(/opt/homebrew/opt/coreutils/bin/gdate -d @$EVENT_TIMESTAMP '+%Y-%m-%d %H:%M:%S');

EVENT_MESSAGE="$APP_NAME -- $EVENT_DATE";

osascript -e 'display dialog "'"${EVENT_MESSAGE}"'" with icon note'
