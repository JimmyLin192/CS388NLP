mode=$1
java -cp "class:lib/mallet-deps.jar" cc.mallet.fst.SimpleTagger \
        --train --model-file wsj.CRF/$mode.model \
        --test lab datasets/wsj_00.mal.$mode datasets/wsj_01.mal.$mode 2> wsj.CRF/$mode.testlog
