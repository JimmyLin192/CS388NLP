CC=javac

all:
	(cd codebases && make) 

tar: 
	zip ngram_XL5224.tgz codebases/*.java codebases/Makefile traces/*.txt README report.pdf Makefile

push:
	git add *.java Makefile traces
	git commit
	git push

clean:
	rm -rf *.class
