#!/bin/bash

mode="all"
i=1

#for i in `seq 1 10`; do
    java -cp "class:lib/mallet-deps.jar" cc.mallet.fst.SimpleTagger --train true --model-file atis3.CRF.$mode/$i.model --training-proportion 0.8 --random-seed $i --test lab datasets/atis3.mal.$mode )
# 2> atis3.CRF.$mode/$i.log 
#done
