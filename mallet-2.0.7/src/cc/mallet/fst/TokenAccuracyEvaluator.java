/* Copyright (C) 2002 Univ. of Massachusetts Amherst, Computer Science Dept.
   This file is part of "MALLET" (MAchine Learning for LanguagE Toolkit).
   http://www.cs.umass.edu/~mccallum/mallet
   This software is provided under the terms of the Common Public License,
   version 1.0, as published by http://www.opensource.org.  For further
   information, see the file `LICENSE' included with this distribution. */




/** 
   @author Andrew McCallum <a href="mailto:mccallum@cs.umass.edu">mccallum@cs.umass.edu</a>
 */

package cc.mallet.fst;


import java.util.HashMap;
import java.util.logging.Logger;

import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.types.Sequence;

import cc.mallet.util.MalletLogger;

/**
 * Evaluates a transducer model based on predictions of individual tokens.
 */
public class TokenAccuracyEvaluator extends TransducerEvaluator
{
	private static Logger logger = MalletLogger.getLogger(TokenAccuracyEvaluator.class.getName());

	private HashMap<String,Double> accuracy = new HashMap<String,Double>();
	private HashMap<Object,Integer> vocabularies = new HashMap<Object,Integer>();
    private static int count = 0;

	public TokenAccuracyEvaluator (InstanceList[] instanceLists, String[] descriptions) {
		super (instanceLists, descriptions);
	}
	
	public TokenAccuracyEvaluator (InstanceList instanceList1, String description1) {
		this (new InstanceList[] {instanceList1}, new String[] {description1});
	}
	
	public TokenAccuracyEvaluator (InstanceList instanceList1, String description1,
			InstanceList instanceList2, String description2) {
		this (new InstanceList[] {instanceList1, instanceList2}, new String[] {description1, description2});
	}
	
	public TokenAccuracyEvaluator (InstanceList instanceList1, String description1,
			InstanceList instanceList2, String description2,
			InstanceList instanceList3, String description3) {
		this (new InstanceList[] {instanceList1, instanceList2, instanceList3}, new String[] {description1, description2, description3});
	}

	public void evaluateInstanceList (TransducerTrainer trainer, InstanceList instances, String description) 
  {
		int numCorrectTokens;
		int totalTokens;
        int numCorrectOOVTokens;
        int totalOOVTokens;

        /*
        if (description.equals("Testing") && vocabularies.size() == 0) {
            FileReader fileReader = new FileReader(new File("vocabularies.txt"));
            BufferedReader br = new BufferedReader(fileReader);
            String line = null;
            // if no more lines the readLine() returns null
            while ((line = br.readLine()) != null) {
                // reading lines until the end of the file
                if (line.length() > 0) {
                    vocabularies.
                }
            }
            */
		Transducer transducer = trainer.getTransducer();
		totalTokens = 0; numCorrectTokens = 0;
        totalOOVTokens = 0; numCorrectOOVTokens = 0;
		for (int i = 0; i < instances.size(); i++) {
			Instance instance = instances.get(i);
			Sequence input = (Sequence) instance.getData();
			Sequence trueOutput = (Sequence) instance.getTarget();
			assert (input.size() == trueOutput.size());
			//System.err.println ("TokenAccuracyEvaluator "+i+" length="+input.size());
			Sequence predOutput = transducer.transduce (input);
			assert (predOutput.size() == trueOutput.size());

			for (int j = 0; j < trueOutput.size(); j++) {
				totalTokens++;
                // for Out-of-vocabulary accounting
                if (description.equals("Training") ) {
                    // keep track of vocabulary set of training dataset
                    if (count == 0)
                        vocabularies.put(input.get(j), new Integer(1));
                } else if (description.equals("Testing")) {
                    // in testing, we particularly monitor those are not in
                    // training set
                    if (!vocabularies.containsKey(input.get(j))) 
                        totalOOVTokens ++;
                } else {
                    // throw exception for else case
                    assert(false);
                }
				if (trueOutput.get(j).equals(predOutput.get(j))) {
					numCorrectTokens++;
                    if (description.equals("Testing")) {
                        if (!vocabularies.containsKey(input.get(j))) 
                            numCorrectOOVTokens ++;
                    }
                }
			}
			//System.err.println ("TokenAccuracyEvaluator "+i+" numCorrectTokens="+numCorrectTokens+" totalTokens="+totalTokens+" accuracy="+((double)numCorrectTokens)/totalTokens);
		}
		double acc = ((double)numCorrectTokens)/totalTokens;
        double OOVacc = ((double)numCorrectOOVTokens)/totalOOVTokens;
		//System.err.println ("TokenAccuracyEvaluator accuracy="+acc);
		accuracy.put(description, acc);
        accuracy.put(description+"OOV", OOVacc);
		logger.info (description +" accuracy="+acc);
        count ++;
        if (description.equals("Testing")) 
            logger.info (description +" OOVaccuracy="+OOVacc);
	}

	/**
	 * Returns the accuracy from the last time test() or evaluate() was called
	 * @return
	 */
	public double getAccuracy (String description)
	{
		Double ret = accuracy.get(description);
		if (ret != null)
			return ret.doubleValue();
		throw new IllegalArgumentException ("No accuracy available for instance list \""+description+"\"");
	}
}
