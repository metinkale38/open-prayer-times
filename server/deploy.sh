#!/bin/bash


cd ..
./gradlew clean :server:shadowJar --refresh-dependencies

scp server/build/libs/server-all.jar root@vps.mk38.de:/srv/open-prayer-times/app.jar
ssh root@vps.mk38.de docker restart open-prayer-times