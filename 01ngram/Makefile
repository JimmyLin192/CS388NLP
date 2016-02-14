CC=javac

all:
	(cd codebases && make) 

tar: 
	tar -czvf ngram_XL5224.tar.gz codebases/*.java README 

push:
	git add *.java Makefile
	git commit
	git push

clean:
	rm -rf *.class
