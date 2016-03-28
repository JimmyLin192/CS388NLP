CS388 Natural Language Processing HW03
=============================

Student Information
-------------------

    Name: Xin Lin
    EID: XL5224
    Email: jimmylin@utexas.edu

Code Modified or Created
------------------

    stanford-parser/MyParser.java      command line interface of self training 
    stanford-parser/WSJPreprocessor.java     preprocessing code 1 
    stanford-parser/BROWNPreprocessor.java   preprocessing code 2
    stanford-parser/Makefile           specific commands to run my codes


Other Directories
-----------------

    condor_files/     directories containing files of condor job descriptions
    matlab_plots/     directories containing files of matlab plotting scripts
    traces/           directories containing files of parsing results and logs


aliasing
-----------------

source: wsj, target: brown
    wbc1  -  out-of-domain testing with adapted learning
    wbc2  -  in-domain testing
    wbc3  -  out-of-domain testing without adapted learning
    wbc4  -  varying self-train size curve

source: brown, target: wsj
    bwc1  -  out-of-domain testing with adapted learning
    bwc2  -  in-domain testing
    bwc3  -  out-of-domain testing without adapted learning
    bwc4  -  varying self-train size curve

    
Compilation
-----------------

To compile the java code I modified and created, command the following in the
stanford-parser/ directory: 

    make


Usage of Command Line Interface
--------------------
Usage:

    java MyParser [trainSeed] [selfTrain] [testSet]

For detailed invocation, you can refer to those condor job descriptions in
condor_files/ directory.


Run Preprocessing Code
-------------------
Usage:
    java WSJPreprocessor [source] [sourceSize] [outfile]
    java BROWNPreprocessor [source] [selfTrainFile] [testFile]

Preprocessing for wsj datasets can be done by executing the following command
in the stanford-parser/ directory:

    make prewsj   

Preprocessing for brown datasets can be done by executing the following command
in the stanford-parser/ directory:

    make prebrown


