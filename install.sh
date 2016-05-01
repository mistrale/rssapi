#!/bin/bash

docker rm -f $(docker ps -a -q)
docker run --name mongodb -d mongo
docker run -p 8081:8081 --name rsstek --link mongodb:mongo -d rsstek
