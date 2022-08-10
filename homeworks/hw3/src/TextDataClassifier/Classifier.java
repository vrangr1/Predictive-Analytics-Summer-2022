package TextDataClassifier;


public class Classifier{

    public static final boolean debugMode = false;

    private static void printMatrix(double[][] matrix, int m, int n){
        System.out.println("Matrix Printing:");
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
        if (debugMode) dataset.printDataset();
        TFIDF.initialize(dataset);
        double[][] tfidfMatrix = TFIDF.doTFIDF(dataset);
        if (debugMode)
        printMatrix(tfidfMatrix, dataset.documentCount, TFIDF.totalWords);
    }
}