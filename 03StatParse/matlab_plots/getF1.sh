#!/bin/bash

for size in 1000 2000 3000 4000 5000 7000 10000 13000 16000 20000 25000 30000 35000;
#for size in 1000 2000 3000 4000 5000 7000 10000 13000 17000 21000; 
do 
    tail -10 ../../traces/bwc4/${size}.err 
done
