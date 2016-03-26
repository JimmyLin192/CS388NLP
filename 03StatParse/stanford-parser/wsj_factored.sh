#/bin/bash

java -cp "stanford-parser.jar:slf4j-api.jar:" -server -mx1500m edu.stanford.nlp.parser.lexparser.LexicalizedParser  \
     -evals "tsv" -goodFactored -maxLength 40 \
     -train ../datasets/wsj/ 200-270  \
     -testTreebank ../datasets/wsj 2000-2100  \
     >  labeled_output2.txt
