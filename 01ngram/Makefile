CC=javac

all:
	(cd codebases && make) 

tar: 
	tar -czvf codebases.tar.gz codebases/*.java

push:
	git add *.java Makefile
	git commit
	git push

clean:
	rm -rf *.class
