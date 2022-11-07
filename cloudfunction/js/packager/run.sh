#!/bin/bash

cd dist || exit
npx @google-cloud/functions-framework --target=api