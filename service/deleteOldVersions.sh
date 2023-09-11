#!/bin/bash

gcloud app versions list --format="value(version.id)" --sort-by="~version.createTime" | tail -n +6 | xargs -r gcloud app versions delete --quiet