#!/bin/sh
#./gradlew shadowJar
gcloud functions deploy jvmapi \
   --entry-point=dev.metinkale.prayertimes.cloudfunction.Api \
   --runtime=java11 \
   --source=build/libs/shadowJar \
   --trigger-http --allow-unauthenticated --region=europe-west1