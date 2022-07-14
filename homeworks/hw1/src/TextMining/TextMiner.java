package TextMining;

import java.util.*;


public class TextMiner{
    private static void printMatrix(double[][] matrix, int m, int n){
        for (int i = 0; i < m; ++i){
            for (int j = 0; j < n; ++j){
                System.out.print(matrix[i][j]);
                if (j < n - 1) System.out.print(", ");
            }
            System.out.print("\n");
        }
    }

    public static void main(String[] args) throws Exception{
        Dataset dataset = PreProcess.doPreProcessing();
        TFIDF.initialize(dataset);
        double[][] tfidfMatrix = TFIDF.doTFIDF(dataset);
        // printMatrix(tfidfMatrix, dataset.documentCount, TFIDF.totalWords);
        // TODO: Do keyword generation for each folder
        kMeans.doKMeans(tfidfMatrix, 3);
    }
}