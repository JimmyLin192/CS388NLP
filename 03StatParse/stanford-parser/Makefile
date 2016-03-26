# This is a rudimentary Makefile for rebuilding the parser.
# We actually use ant (q.v.) or a Java IDE.

JAVAC = javac
JAVAFLAGS = -O -d classes -encoding utf-8


all:
	javac -cp "stanford-parser.jar:slf4j-api.jar:slf4j-simple.jar:" MyParser.java

run:
	java -cp "stanford-parser.jar:slf4j-api.jar:slf4j-simple.jar:stanford-parser-3.6.0-models.jar:" MyParser

clean: 
	rm -rf *.class

parser:
	mkdir -p classes
	$(JAVAC) $(JAVAFLAGS) src/edu/stanford/nlp/*/*.java \
	    src/edu/stanford/nlp/*/*/*.java src/edu/stanford/nlp/*/*/*/*.java
	cd classes ; jar -cfm ../stanford-parser-`date +%Y-%m-%d`.jar ../src/edu/stanford/nlp/parser/lexparser/lexparser-manifest.txt edu ; cd ..
	cp stanford-parser-`date +%Y-%m-%d`.jar stanford-parser.jar
	rm -rf classes
