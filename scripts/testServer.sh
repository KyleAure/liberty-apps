#!/bin/bash

ROOT_URL="http://localhost:9080/module-jdbc-mbean?testMethod="

curl "${ROOT_URL}getConnections&connections=5"
curl "${ROOT_URL}showPoolContents"
curl "${ROOT_URL}purgeAbort"

echo "TESTING DONE!"