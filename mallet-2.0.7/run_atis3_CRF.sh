#!/bin/bash

mode="all"
i=2

#for i in `seq 1 10`; do
    java -cp "class:lib/mallet-deps.jar" cc.mallet.fst.SimpleTagger  \
        --train true --model-file $i.model \
        --training-proportion 0.8 \
        --random-seed $i --test lab ../02POS/datasets/atis3.mal.$mode 2> log 
# 2> atis3.CRF.$mode/$i.log 
#done
