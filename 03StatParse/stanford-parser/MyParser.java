
import java.util.Collection;
import java.util.List;
import java.io.StringReader;

import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.parser.lexparser.Options;
import edu.stanford.nlp.parser.lexparser.EvaluateTreebank;

class MyParser {

    /* Detailing Usage of this java script */
    public static void usage() {
        String cmd_usage = "Usage: \n\t java MyParser [trainSeed] [selfTrain] [testSet]";
        System.out.println(cmd_usage);
    }

    /**
     * The main method demonstrates the easiest way to load a parser.
     * Usage: {@code java MyParser [trainSeed] [selfTrain] [testSet]}
     */
    public static void main(String[] args) {
        /* process input arguments */
        if (args.length == 3) {
            String tsFile = args[0]; // Train Seed file name
            String sfFile = args[1]; // Self Train file name
            String testFile = args[2]; // Test Set file name
            selfTrain (tsFile, sfFile, testFile);
        }
        else {
            System.out.println("Incorrect Calling!");
            usage();
            return ;
        }
    }

    /* Self Training Parser */
    public static void selfTrain (String seedFile, String stFile, String testFile) {
        System.out.println("Train Seed Set Filename: " + seedFile);
        System.out.println("Self Train Set Filename: " + stFile);
        System.out.println("Test Set Filename: " + testFile);
        // 0. load 
        Treebank selftrainTB = new MemoryTreebank();
        selftrainTB.loadPath(stFile);

        // 1. Train Seed Set
        Options op = new Options();
        op.doDep = false;
        op.doPCFG = true;
        op.setOptions("-goodPCFG", "-evals", "tsv");
        Treebank seedTB = op.tlpParams.diskTreebank();
        seedTB.loadPath(seedFile);
        LexicalizedParser lpc = LexicalizedParser.trainFromTreebank(seedTB, op);

        // 2. Annotate Self Train Set
        MemoryTreebank buffTB = new MemoryTreebank();
        System.out.println("Adding seedTB to buffTB...");
        for (Tree treeInstance: seedTB) buffTB.add(treeInstance);
        System.out.println("Adding annotatedTree to buffTB..");
        for (Tree treeInstance : selftrainTB) {
            Tree annotatedTree = lpc.apply(Sentence.toCoreLabelList(treeInstance.yieldWords()));
            buffTB.add(annotatedTree);
        }

        // 3. Retrain with both Seed Set and Self-Trained Set
        System.out.println("Training self-train set..");
        lpc = LexicalizedParser.trainFromTreebank(buffTB, op);

        // 4. Test on Testing Set
        Treebank testTB = op.tlpParams.diskTreebank();
        testTB.loadPath(testFile);
        EvaluateTreebank evaluator = new EvaluateTreebank (lpc);
        evaluator.testOnTreebank(testTB);
        return ;
    }

    private MyParser() {} // static methods only

}
