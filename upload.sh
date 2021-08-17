#!/bin/bash

./gradlew :nav_annotation:uploadArchives
./gradlew :nav_api:uploadArchives
./gradlew :nav_test:uploadArchives
./gradlew :nav_doc:uploadArchives
./gradlew :nav_compiler:uploadArchives
./gradlew :register:uploadArchives