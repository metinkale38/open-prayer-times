#!/bin/sh
#./gradlew shadowJar
gcloud functions deploy api \
   --entry-point=com.metinkale.prayertimes.cloudfunctions.App \
   --runtime=java11 \
   --trigger-http \
   --source=build/libs/shadowJar \
   --region=europe-west3 \
  --allow-unauthenticated \
  --security-level=secure-always