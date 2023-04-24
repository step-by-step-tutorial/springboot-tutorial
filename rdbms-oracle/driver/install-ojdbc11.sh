#!/usr/bin/env bash

mvn install:install-file \
-Dfile=./ojdbc11.jar \
-DgroupId=com.oracle \
-DartifactId=ojdbc11 \
-Dversion=21c \
-Dpackaging=jar
