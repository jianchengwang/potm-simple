#!/bin/bash

BASEDIR=$(cd "$(dirname "$0")" && pwd)
cd "${BASEDIR}" || exit

# create docker network
docker network create --driver bridge potm_network

# create docker volume
docker volume create potm_mysql_data
docker volume create potm_redis_data