# This is a rudimentary Makefile for rebuilding the parser.
# We actually use ant (q.v.) or a Java IDE.

JAVAC = javac
JAVAFLAGS = -O -d classes -encoding utf-8

GIT_TRACK_FILE=Makefile MyParser.java wsj_factored.sh wsj_pcfg.sh WSJPreprocessor.java


all:
	javac -cp "stanford-parser.jar:slf4j-api.jar:slf4j-simple.jar:" MyParser.java SourcePreprocessor.java

source:
	java -cp "stanford-parser.jar:slf4j-api.jar:slf4j-simple.jar:stanford-parser-3.6.0-models.jar:" SourcePreprocessor ../datasets/wsj/ 1000 ../datasets/wsj_1000.mrg

run:
	java -cp "stanford-parser.jar:slf4j-api.jar:slf4j-simple.jar:stanford-parser-3.6.0-models.jar:" MyParser ../datasets/wsj/02/wsj_0200.mrg ../datasets/brown/cf/cf01.mrg ../datasets/brown/cl/cl01.mrg

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
