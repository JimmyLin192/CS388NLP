#!/bin/bash

i=$1
for i in `seq 1 10`; do
        (time java -cp "class:lib/mallet-deps.jar" cc.mallet.fst.HMMSimpleTagger \
        --train true --model-file wsj.HMM/$i.model \
        --training-proportion 0.5 \
        --random-seed $i \
        --test lab datasets/wsj.mal 2> wsj.HMM/$i.trainlog) &
    done
