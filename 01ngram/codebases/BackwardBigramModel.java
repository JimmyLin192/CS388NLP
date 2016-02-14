package codebases;

import java.io.*;
import java.util.*;

/** 
 * @author Xin Lin (xl5224, jimmylin@utexas.edu)
 * A simple bigram language model that uses simple fixed-weight interpolation
 * with a unigram model for smoothing.
*/

public class BackwardBigramModel extends BigramModel {
    /** Accumulate unigram and bigram counts for this sentence */
    public void trainSentence (List<String> sentence) {
        // First count an initial start sentence token
        String nextToken = "</S>";
        DoubleValue unigramValue = unigramMap.get("</S>");
        unigramValue.increment();
        tokenCount++;
        // For each token in sentence, accumulate a unigram and bigram count
        int nTokens = sentence.size();
        for (int i = nTokens - 1; i > 0; i --) {
            String token = sentence.get(i);
            unigramValue = unigramMap.get(token);
            // If this is the first time token is seen then count it
            // as an unkown token (<UNK>) to handle out-of-vocabulary 
            // items in testing
            if (unigramValue == null) {
                // Store token in unigram map with 0 count to indicate that
                // token has been seen but not counted
                unigramMap.put(token, new DoubleValue());
                token = "<UNK>";
                unigramValue = unigramMap.get(token);
            }
            unigramValue.increment();    // Count unigram
            tokenCount++;               // Count token
            // Make bigram string 
            String bigram = bigram(nextToken, token);
            DoubleValue bigramValue = bigramMap.get(bigram);
            if (bigramValue == null) {
                // If previously unseen bigram, then
                // initialize it with a value
                bigramValue = new DoubleValue();
                bigramMap.put(bigram, bigramValue);
            }
            // Count bigram
            bigramValue.increment();
            nextToken = token;
        }
        // Account for end of sentence unigram
        unigramValue = unigramMap.get("<S>");
        unigramValue.increment();
        tokenCount++;
        // Account for end of sentence bigram
        String bigram = bigram(nextToken, "<S>");
        DoubleValue bigramValue = bigramMap.get(bigram);
        if (bigramValue == null) {
            bigramValue = new DoubleValue();
            bigramMap.put(bigram, bigramValue);
        }
        bigramValue.increment();
    }

    /* Compute log probability of sentence given current model */
    public double sentenceLogProb (List<String> sentence) {
	// Set start-sentence as initial token
	String nextToken = "</S>";
	// Maintain total sentence prob as sum of individual token
	// log probs (since adding logs is same as multiplying probs)
    double sentenceLogProb = 0;
    // Check prediction of each token in sentence
    int nTokens = sentence.size();
    for (int i = nTokens - 1; i > 0; i --) {
        String token = sentence.get(i);
        // Retrieve unigram prob
        DoubleValue unigramVal = unigramMap.get(token);
        if (unigramVal == null) {
            // If token not in unigram model, treat as <UNK> token
            token = "<UNK>";
            unigramVal = unigramMap.get(token);
        }
        // Get bigram prob
        String bigram = bigram(nextToken, token);
        DoubleValue bigramVal = bigramMap.get(bigram);
        // Compute log prob of token using interpolated prob of unigram and bigram
        double logProb = Math.log(interpolatedProb(unigramVal, bigramVal));
        // Add token log prob to sentence log prob
        sentenceLogProb += logProb;
        // update previous token and move to next token
        nextToken = token;
    }
    // Check prediction of end of sentence token
    DoubleValue unigramVal = unigramMap.get("<S>");
    String bigram = bigram(nextToken, "<S>");
    DoubleValue bigramVal = bigramMap.get(bigram);
    double logProb = Math.log(interpolatedProb(unigramVal, bigramVal));
    // Update sentence log prob based on prediction of <S>
    sentenceLogProb += logProb;
    return sentenceLogProb;
    }

    /** Like sentenceLogProb but excludes predicting end-of-sentence when computing prob */
    public double sentenceLogProb2 (List<String> sentence) {
        String nextToken = "</S>";
        double sentenceLogProb = 0;
        int nTokens = sentence.size();
        for (int i = nTokens - 1; i > 0; i --) {
            String token = sentence.get(i);
            DoubleValue unigramVal = unigramMap.get(token);
            if (unigramVal == null) {
                token = "<UNK>";
                unigramVal = unigramMap.get(token);
            }
            String bigram = bigram(nextToken, token);
            DoubleValue bigramVal = bigramMap.get(bigram);
            double logProb = Math.log(interpolatedProb(unigramVal, bigramVal));
            sentenceLogProb += logProb;
            nextToken = token;
        }
        return sentenceLogProb;
    }

    /** Train and test a backward bigram model.
     *  Command format: "codebases.BackwardBigramModel [DIR]* [TestFrac]" where DIR 
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
	List<List<String>> sentences = POSTaggedFile.convertToTokenLists(files);
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
	// Create a bigram model and train it.
	BackwardBigramModel model = new BackwardBigramModel();
	System.out.println("Training...");
	model.train(trainSentences);
	// Test on training data using test and test2
	model.test(trainSentences);
	model.test2(trainSentences);
	System.out.println("Testing...");
	// Test on test data using test and test2
	model.test(testSentences);
	model.test2(testSentences);
    }

}
