#!/bin/bash

mod=$1
i=1
#for i in `seq 1 10`; do
        (time java -cp "class:lib/mallet-deps.jar" cc.mallet.fst.SimpleTagger \
        --train true --model-file wsj.CRF.$mod/$i.model \
        --training-proportion 0.5 \
        --random-seed $i \
        --test lab datasets/wsj.mal.$mod 2> wsj.CRF.$mod/$i.log) &
#    done
