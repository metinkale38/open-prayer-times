#!/bin/bash

cd build || exit
gcloud functions deploy api --runtime nodejs16 --trigger-http --allow-unauthenticated --region=europe-west1