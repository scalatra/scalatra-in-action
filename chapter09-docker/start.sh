#!/bin/bash

BASE=$(dirname $(readlink -f $0))

docker run -d \
  -v $BASE/data:/app/data \
  -v $BASE/conf:/app/conf:ro \
  -p 8080:80 \
  --name chapter09-standalone \
  org.scalatra/chapter09-docker

