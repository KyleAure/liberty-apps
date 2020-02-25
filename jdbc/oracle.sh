#!/bin/bash

#Stop Previous Containers
docker stop container_oracle
docker rm container_oracle
docker stop container_app_jdbcoracle
docker rm container_app_jdbcoracle
docker rmi app_jdbcoracle

#Build Oracle
docker run -d -p 49161:1521 --name container_oracle oracleinanutshell/oracle-xe-11g

#package app
mvn package

#Build app
docker build -t app_jdbcoracle .
docker run -d -p 9080:9080 -p 9443:9443 --name container_app_jdbcoracle app_jdbcoracle

read -p "Wait for liberty to start. Then press enter to continue"

#Open site
curl -i 'http://localhost:9080/jdbcoracle/insert?key=1&value=Kyle'
curl -i 'http://localhost:9080/jdbcoracle/insert?key=2&value=Riley'
curl -i 'http://localhost:9080/jdbcoracle/insert?key=3&value=Bri'
curl -i http://localhost:9080/jdbcoracle
open 'http://localhost:9080/jdbcoracle'

