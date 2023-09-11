#!/bin/bash

cat cities500.txt | grep -e "	PPLG	" -e "	PPLC	" -e "	PPLA	" -e "	PPLA1	" -e "	PPLA2	" -e "	PPLA3	" -e "	PPLA4	" -e "	PPLA5	" | cut -d"	" -f2,4,5,6,9,17,18 > src/jvmMain/resources/tsv/geonames.tsv
