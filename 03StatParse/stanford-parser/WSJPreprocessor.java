
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

class WSJPreprocessor {

    /* Detailing Usage of this java script */
    public static void usage() {
        String cmd_usage = "Usage: \n\t java SourcePreprocessor [source] [sourceSize] [outfile]";
        System.out.println(cmd_usage);
    }

    /**
     * The main method demonstrates the easiest way to load a parser.
     */
    public static void main(String[] args) throws FileNotFoundException {
        /* process input arguments */
        if (args.length == 3) {
            String sourcePath = args[0]; 
            int sourceSize = Integer.parseInt(args[1]);
            String outFile = args[2]; 
            preprocess (sourcePath, sourceSize, outFile);
        }
        else {
            System.out.println("Incorrect Calling!");
            usage();
        }
    }

    public static void preprocess (String sourcePath, int sourceSize, String outFile) throws FileNotFoundException {
        System.out.println("Source Pathname: " + sourcePath);

        // 1. load all source trees
        Options op = new Options();
        Treebank sourceTB = op.tlpParams.diskTreebank();
        sourceTB.loadPath(sourcePath);

        // 2. restore only first [sourceSize] trees
        MemoryTreebank buffTB = new MemoryTreebank();
        int count = 0;
        for (Tree treeInstance: sourceTB) {
            if (count < sourceSize) {
                buffTB.add(treeInstance);
                count ++;
            } else break;
        }

        // 3. output to outfile
        System.out.println("Output Filename: " + outFile);
        System.setOut(new PrintStream(new File(outFile)));
        for (Tree treeInstance: buffTB) {
            treeInstance.pennPrint();
        }
        return ;
    }

    private WSJPreprocessor() {} // static methods only

}
