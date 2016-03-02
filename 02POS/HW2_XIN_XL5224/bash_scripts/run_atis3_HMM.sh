#!/bin/bash

for i in `seq 1 10`; do
    (time java -cp "class:lib/mallet-deps.jar" cc.mallet.fst.HMMSimpleTagger \
        --train true --model-file atis3.HMM/$i.model \
        --training-proportion 0.8 \
        --random-seed $i \
        --test lab datasets/atis3.mal 2> atis3.HMM/$i.log ) &
	done
