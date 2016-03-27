
import java.util.Collection;
import java.util.List;
import java.io.StringReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

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

class BROWNPreprocessor {

    /* Detailing Usage of this java script */
    public static void usage() {
        String cmd_usage = "Usage: \n\t java BROWNPreprocessor [source] [selfTrainFile] [testFile]";
        System.out.println(cmd_usage);
    }

    /**
     * The main method demonstrates the easiest way to load a parser.
     */
    public static void main(String[] args) throws FileNotFoundException {
        /* process input arguments */
        if (args.length == 3) {
            String sourcePath = args[0]; 
            String selfTrainFile = args[1]; 
            String testFile = args[2]; 
            preprocess (sourcePath, selfTrainFile, testFile);
        }
        else {
            System.out.println("Incorrect Calling!");
            usage();
        }
    }

    public static void preprocess (String sourcePath, String selfTrainFile, String testFile) throws FileNotFoundException {
        System.out.println("Source Pathname: " + sourcePath);
        File source = new File (sourcePath);

        // 1. load all source trees
        Treebank selfTrainTB = new MemoryTreebank ();
        Treebank testTB = new MemoryTreebank ();
        Options op = new Options();
        for (File subdir : source.listFiles()) { // walk through all directory
            if (!subdir.isDirectory()) continue;
            Treebank sourceTB = op.tlpParams.diskTreebank();
            sourceTB.loadPath(subdir);
            int count = 0;
            int threshold = sourceTB.size() * 9 / 10;
            for (Tree treeInstance : sourceTB) { // walk through all trees
                if (count < threshold) selfTrainTB.add(treeInstance);
                else testTB.add(treeInstance);
                count ++;
            }
        }

        // 3. output to outfile
        System.out.println("Self Train Set Filename: " + selfTrainFile);
        System.setOut(new PrintStream(new File(selfTrainFile)));
        for (Tree treeInstance: selfTrainTB) treeInstance.pennPrint();

        System.setOut(new PrintStream(new File(testFile)));
        for (Tree treeInstance: testTB) treeInstance.pennPrint();
        return ;
    }

    private BROWNPreprocessor() {} // static methods only

}
