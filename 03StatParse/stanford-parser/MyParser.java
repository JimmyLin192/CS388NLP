
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
        // 0. load all
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
        System.out.println("adding seedTB to buffTB...");
        for (Tree treeInstance: seedTB) buffTB.add(treeInstance);
        System.out.println("adding annotatedTree to buffTB..");
        int count = 0;
        for (Tree treeInstance : selftrainTB) {
            Tree annotatedTree = lpc.apply(Sentence.toCoreLabelList(treeInstance.yieldWords()));
            buffTB.add(annotatedTree);
            if ((count) % 10 == 0) System.out.println(count);
            count ++;
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

    /**
     * demoDP demonstrates turning a file into tokens and then parse
     * trees.  Note that the trees are printed by calling pennPrint on
     * the Tree object.  It is also possible to pass a PrintWriter to
     * pennPrint if you want to capture the output.
     * This code will work with any supported language.
     */
    public static void demoDP(LexicalizedParser lp, String filename) {
        // This option shows loading, sentence-segmenting and tokenizing
        // a file using DocumentPreprocessor.
        TreebankLanguagePack tlp = lp.treebankLanguagePack(); // a PennTreebankLanguagePack for English
        GrammaticalStructureFactory gsf = null;
        if (tlp.supportsGrammaticalStructures()) {
            gsf = tlp.grammaticalStructureFactory();
        }
        // You could also create a tokenizer here (as below) and pass it
        // to DocumentPreprocessor
        for (List<HasWord> sentence : new DocumentPreprocessor(filename)) {
            Tree parse = lp.apply(sentence);
            parse.pennPrint();
            System.out.println();

            if (gsf != null) {
                GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
                Collection tdl = gs.typedDependenciesCCprocessed();
                System.out.println(tdl);
                System.out.println();
            }
        }
    }

    /**
     * demoAPI demonstrates other ways of calling the parser with
     * already tokenized text, or in some cases, raw text that needs to
     * be tokenized as a single sentence.  Output is handled with a
     * TreePrint object.  Note that the options used when creating the
     * TreePrint can determine what results to print out.  Once again,
     * one can capture the output by passing a PrintWriter to
     * TreePrint.printTree. This code is for English.
     */
    public static void demoAPI(LexicalizedParser lp) {
        // This option shows parsing a list of correctly tokenized words
        String[] sent = { "This", "is", "an", "easy", "sentence", "." };
        List<CoreLabel> rawWords = Sentence.toCoreLabelList(sent);
        Tree parse = lp.apply(rawWords);
        parse.pennPrint();
        System.out.println();

        // This option shows loading and using an explicit tokenizer
        String sent2 = "This is another sentence.";
        TokenizerFactory<CoreLabel> tokenizerFactory =
            PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
        Tokenizer<CoreLabel> tok =
            tokenizerFactory.getTokenizer(new StringReader(sent2));
        List<CoreLabel> rawWords2 = tok.tokenize();
        parse = lp.apply(rawWords2);

        TreebankLanguagePack tlp = lp.treebankLanguagePack(); // PennTreebankLanguagePack for English
        GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
        GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
        List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();
        System.out.println(tdl);
        System.out.println();

        // You can also use a TreePrint object to print trees and dependencies
        TreePrint tp = new TreePrint("penn,typedDependenciesCollapsed");
        tp.printTree(parse);
    }

    private MyParser() {} // static methods only

}
