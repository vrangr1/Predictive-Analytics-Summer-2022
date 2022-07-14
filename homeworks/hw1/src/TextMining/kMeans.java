package TextMining;

import edu.stanford.nlp.simple.*;
import java.util.*;

public class kMeans{
    enum METRIC{
        EUCLIDEAN,
        COSINE
    }

    private static int documentCount, totalWords;
    
    public static void doKMeans(double[][] tfidfMatrix, int k){
        documentCount = tfidfMatrix.length;
        totalWords = tfidfMatrix[0].length;
    }
}