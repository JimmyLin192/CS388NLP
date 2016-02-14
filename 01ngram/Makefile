CC=javac

all:
	(cd codebases && make) 

tar: 
	tar -czvf ngram_XL5224.gtz codebases/*.java traces/*.txt README report/report.pdf

push:
	git add *.java Makefile traces
	git commit
	git push

clean:
	rm -rf *.class
