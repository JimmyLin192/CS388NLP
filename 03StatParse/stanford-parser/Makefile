# This is a rudimentary Makefile for rebuilding the parser.
# We actually use ant (q.v.) or a Java IDE.

JAVAC = javac
JAVAFLAGS = -O -d classes -encoding utf-8

GIT_TRACK_FILE=Makefile MyParser.java wsj_factored.sh wsj_pcfg.sh WSJPreprocessor.java BROWNPreprocessor.java


all:
	javac -cp "stanford-parser.jar:slf4j-api.jar:slf4j-simple.jar:" MyParser.java WSJPreprocessor.java BROWNPreprocessor.java

prewsj:
	for size in 1000 2000 3000 4000 5000 7000 10000 13000 16000 20000 25000 30000 35000; do \
		java -cp "stanford-parser.jar:slf4j-api.jar:slf4j-simple.jar:stanford-parser-3.6.0-models.jar:" WSJPreprocessor ../datasets/wsj/ $$size ../datasets/wsj_$$size.mrg; \
	done

prebrown:
	java -cp "stanford-parser.jar:slf4j-api.jar:slf4j-simple.jar:stanford-parser-3.6.0-models.jar:" BROWNPreprocessor ../datasets/brown/ ../datasets/st_brown.mrg ../datasets/test_brown; 
	for size in 1000 2000 3000 4000 5000 7000 10000 13000 17000 21000; do \
		java -cp "stanford-parser.jar:slf4j-api.jar:slf4j-simple.jar:stanford-parser-3.6.0-models.jar:" WSJPreprocessor ../datasets/st_brown.mrg $$size ../datasets/st_brown_$$size.mrg; \
	done

run:
	java -cp "stanford-parser.jar:slf4j-api.jar:slf4j-simple.jar:stanford-parser-3.6.0-models.jar:" MyParser ../datasets/wsj_1000.mrg ../datasets/st_empty.mrg ../datasets/test_wsj23.mrg

clean: 
	rm -rf *.class

push:
	git add $(GIT_TRACK_FILE)
	git commit 
	git push

parser:
	mkdir -p classes
	$(JAVAC) $(JAVAFLAGS) src/edu/stanford/nlp/*/*.java \
	    src/edu/stanford/nlp/*/*/*.java src/edu/stanford/nlp/*/*/*/*.java
	cd classes ; jar -cfm ../stanford-parser-`date +%Y-%m-%d`.jar ../src/edu/stanford/nlp/parser/lexparser/lexparser-manifest.txt edu ; cd ..
	cp stanford-parser-`date +%Y-%m-%d`.jar stanford-parser.jar
	rm -rf classes
