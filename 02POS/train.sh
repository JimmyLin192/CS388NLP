/usr/bin/time -o wsj00.time -a java -cp "class:lib/mallet-deps.jar" cc.mallet.fst.SimpleTagger \
        --train true --model-file wsj00.model \
        --training-proportion 1.0 \
        --random-seed 1 --test lab datasets/wsj_00.mal 2> wsj00.log
