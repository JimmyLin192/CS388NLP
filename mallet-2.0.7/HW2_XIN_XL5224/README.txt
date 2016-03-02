CS388 Natural Language Processing HW02
=============================

Student Information
-------------------

    Name: Xin Lin
    EID: XL5224
    Email: jimmylin@utexas.edu

Submission Directory
------------------
   -- report.pdf
   -- README.txt
   -- mallet_codebase/
       - FeatureVector.java
       - TokenAccuracyEvaluator.java
   -- mallet_pos/
       - atis.mal.*  
          ... (replace * with simple,suffix,hyphen,all)
       - wsj_00.mal.*
          ... (replace * with simple,suffix,hyphen,all)
       - wsj_01.mal.*
          ... (replace * with simple,suffix,hyphen,all)
   -- converter_codebase/ 
       - preprocess.py
   -- condor_scripts/
       - cjob.simple.wsj
       - cjob.hyphen.wsj
       - cjob.caps.wsj
       - cjob.suffix.wsj
       - cjob.all.wsj
   -- bash_scripts/
       - run_atis3_HMM.sh
       - run_wsj_HMM.sh
       - run_atis3_CRF.sh

Mallet Code modified
------------------
We add OOV accuracy evaluation to
    src/cc/mallet/fst/TokenAccuracyEvaluator.java

Also add hashcode() and equals() to 
    src/cc/mallet/types/FeatureVector.java

Preprocessing Code
-------------------

    python preprocess.py [OrthOption] [pos_file(directory)] [mallet_file]

where [OrthoOption] can be one of following:

    -simple 
    -suffix
    -hyphen
    -caps
    -all

and [pos_file(directory)] can be a file or a directory. When a directory is
specified, the script will recursively search all pos files within that
directory.

[mallet_file] is the output file in mallet-required format.

Scripts for Experiments
----------------
The following files are bash files that includes commands for training HMM
models for atis3 and wsj datasets:

    run_atis3_HMM.sh
    run_wsj_HMM.sh

Also bash scripts for training CRF models for atis3 datasets:

    run_atis3_CRF.sh

Condor Job file
-----------------
The following files are job specfication files submitted to condor systems
to train CRF model for the wsj dataset with or without orthographic features.
    
    cjob.simple.wsj
    cjob.hyphen.wsj
    cjob.caps.wsj
    cjob.suffix.wsj
    cjob.all.wsj

