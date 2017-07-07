#!/bin/bash

PRICES=$(cut -d',' -f1 src/main/resources/prices.csv)

while true; do
    for price in $PRICES; do
        curl "http://localhost:8080/prices/${price}"
    done
done
