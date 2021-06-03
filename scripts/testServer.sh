#!/bin/bash

ROOT_URL="http://localhost:9080/module-jdbc-mbean"
SECONDS_TO_RUN=150

function getConnection() {
    local SLEEP_OFFSET=2
    for ((i=0 ; i <= $(($SECONDS_TO_RUN * $SLEEP_OFFSET)) ; i++)); do
        echo "Get connection: $i"
        curl "$ROOT_URL?testMethod=getConnection" &> /dev/null & 
        sleep 0.5
    done
}

function purgeWithAbort() {
    local SLEEP_OFFSET=30
    for ((i=0 ; i <= $(($SECONDS_TO_RUN / $SLEEP_OFFSET)) ; i++)); do
        echo "Pool contents before: $i"
        curl "$ROOT_URL?testMethod=showPoolContents"

        echo "Pool contents after: $i"
        curl "$ROOT_URL?testMethod=purgeImmediate"
        sleep $SLEEP_OFFSET
    done
}

purgeWithAbort &
getConnection
wait
curl "$ROOT_URL?testMethod=showPoolContents"
echo "TESTING DONE!"