#!/bin/bash

mode=$1
mkdir atis3.CRF.$mode
for i in `seq 1 5`; do
    (/usr/bin/time -o atis3.CRF.$mode/runtime -a java -cp "class:lib/mallet-deps.jar" cc.mallet.fst.SimpleTagger \
        --train true --model-file atis3.CRF.$mode/$i.model \
        --training-proportion 0.8 \
        --random-seed $i --test lab datasets/atis3.mal.$mode 2> atis3.CRF.$mode/$i.log ) &
done
