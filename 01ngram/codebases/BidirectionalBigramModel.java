package codebases;

import java.io.*;
import java.util.*;

/** 
 * @author Xin Lin (xl5224, jimmylin@utexas.edu)
 * A simple bigram language model that uses simple fixed-weight interpolation
 * with a unigram model for smoothing.
 */

public class BidirectionalBigramModel extends BigramModel {

    private BigramModel forwardModel;
    private BackwardBigramModel backwardModel;

    public BidirectionalBigramModel () {
        // even interpolation between forward and backward model
        this.lambda1 = 0.5;
        this.lambda2 = 0.5;
        // initialize forward and backward model
        this.forwardModel = new BigramModel();
        this.backwardModel = new BackwardBigramModel();
    }

    /** Train the model on a List of sentences represented as
     *  Lists of String tokens */
    public void train (List<List<String>> sentences) {
        // train forward and backward model
        this.forwardModel.train(sentences);
        this.backwardModel.train(sentences);
    }
    public double interpolatedProb (double forwardVal, double backwardVal) {
        return this.lambda1 * forwardVal + this.lambda2 * backwardVal;
    }

    /** TODO: Like sentenceLogProb but excludes predicting end-of-sentence when computing prob */
    public double sentenceLogProb2 (List<String> sentence) {
        String prevToken = "<S>";
        double sentenceLogProb = 0;
        int nToken = sentence.size(); // end-of-sentence exclusive
        for (int i = 0; i < nToken;  i++) {
            String token = sentence.get(i);
            String nextToken = (i<nToken-1)?sentence.get(i+1):"</S>";
            // compute probability in forward model
            DoubleValue f1g = forwardModel.unigramMap.get(token);
            if (f1g == null) {
                token = "<UNK>";
                f1g = forwardModel.unigramMap.get(token);
            }
            String bigram = bigram(prevToken,token);
            DoubleValue f2g = forwardModel.bigramMap.get(bigram);
            double forwardVal = forwardModel.interpolatedProb(f1g, f2g);
            // System.out.println(forwardVal);
            // compute probability in backward model
            DoubleValue b1g = backwardModel.unigramMap.get(token);
            if (b1g == null) {
                token = "<UNK>";
                b1g = backwardModel.unigramMap.get(token);
            }
            DoubleValue b2g = backwardModel.bigramMap.get(bigram(nextToken,token));
            double backwardVal = backwardModel.interpolatedProb(b1g, b2g);
            // System.out.println(backwardVal);
            // compute interpolated probability 
            sentenceLogProb += Math.log(this.interpolatedProb(forwardVal, backwardVal));
            prevToken = token;
        }
        return sentenceLogProb;
    }

    /** Train and test a bidirectional bigram model.
     *  Command format: "codebases/BidirectionalBigramModel [DIR]* [TestFrac]" where DIR 
     *  is the name of a file or directory whose LDC POS Tagged files should be 
     *  used for input data; and TestFrac is the fraction of the sentences
     *  in this data that should be used for testing, the rest for training.
     *  0 < TestFrac < 1
     *  Uses the last fraction of the data for testing and the first part
     *  for training.
     */
    public static void main(String[] args) throws IOException {
        // All but last arg is a file/directory of LDC tagged input data
        File[] files = new File[args.length - 1];
        for (int i = 0; i < files.length; i++) 
            files[i] = new File(args[i]);
        // Last arg is the TestFrac
        double testFraction = Double.valueOf(args[args.length -1]);
        // Get list of sentences from the LDC POS tagged input files
        List<List<String>> sentences = 	POSTaggedFile.convertToTokenLists(files);
        int numSentences = sentences.size();
        // Compute number of test sentences based on TestFrac
        int numTest = (int)Math.round(numSentences * testFraction);
        // Take test sentences from end of data
        List<List<String>> testSentences = sentences.subList(numSentences - numTest, numSentences);
        // Take training sentences from start of data
        List<List<String>> trainSentences = sentences.subList(0, numSentences - numTest);
        System.out.println("# Train Sentences = " + trainSentences.size() + 
                " (# words = " + wordCount(trainSentences) + 
                ") \n# Test Sentences = " + testSentences.size() +
                " (# words = " + wordCount(testSentences) + ")");

        // Create a bidirectional bigram model and train it.
        BidirectionalBigramModel bdmodel = new BidirectionalBigramModel();
        System.out.println("Training...");
        bdmodel.train(trainSentences);
        // Test on training data using test and test2
        // bdmodel.test(trainSentences);
        bdmodel.test2(trainSentences);
        System.out.println("Testing...");
        // Test on test data using test and test2
        // bdmodel.test(testSentences);
        bdmodel.test2(testSentences);
    }

}
