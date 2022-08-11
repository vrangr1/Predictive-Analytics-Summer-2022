package TextDataClassifier;

import java.text.DecimalFormat;
import java.io.*;
public class Classifier{

    public static final boolean debugMode = false;
    public static final boolean toDoFuzzykNN = true;
    public static int k = 5;

    public static void printMatrix(double[][] matrix, int m, int n){
        System.out.println("Matrix Printing:");
        for (int i = 0; i < m; ++i){
            for (int j = 0; j < n; ++j){
                System.out.print(matrix[i][j]);
                if (j < n - 1) System.out.print(", ");
            }
            System.out.print("\n");
        }
    }

    public static void printMatrix(double[][] matrix){
        System.out.println("Matrix Printing:");
        DecimalFormat numberFormat = new DecimalFormat("#0.00");
        // System.out.println(numberFormat.format(number));
        int m = matrix.length, n = matrix[0].length;
        for (int i = 0; i < m; ++i){
            for (int j = 0; j < n; ++j){
                System.out.print(numberFormat.format(matrix[i][j]));
                if (j < n - 1) System.out.print(", ");
            }
            System.out.print("\n");
        }
    }

    public static void writeMatrix(double[][] matrix) throws Exception{
        File file = new File(constants.topicsFile);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))){

        }
    }

    public static void printVec(double[] vector){
        System.out.println("Vector Printing:");
        DecimalFormat numberFormat = new DecimalFormat("#0.00");
        // System.out.println(numberFormat.format(number));
        int m = vector.length;
        for (int i = 0; i < m; ++i){
            System.out.print(numberFormat.format(vector[i]));
            if (i < m - 1) System.out.print(", ");
        }
        System.out.print("\n");
    }

    public static void main(String[] args) throws Exception{
        Dataset dataset = PreProcess.doPreProcessing();
        if (debugMode) dataset.printDataset();
        dataset.tfidfObject = new TFIDF(dataset);
        double[][] tfidfMatrix = dataset.tfidfObject.doTFIDF(dataset);
        
        if (debugMode)
        printMatrix(tfidfMatrix, dataset.documentCount, dataset.tfidfObject.totalWords);
        
        keywordGen.generateKeywords(tfidfMatrix, dataset);
        
        for (k = 2; k < 11; ++k){
            File file = new File(constants.topicsFile);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))){
                writer.write("\nk Value: " + k + "\n");
            }
            System.out.println("\nk Value: " + k);
            
            kNN kNNObject = new kNN(k);

            kNNObject.knnTraining(tfidfMatrix, dataset);
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))){
                writer.write("Cluster Count: " + kNNObject.clusterCount + "\n\n");
            }
            
            Dataset unknownDataset = kNNObject.knnTesting(constants.unknownFolderPath, tfidfMatrix, dataset.tfidfObject);
            
            Evaluator.readTestFilesGroundTruth(unknownDataset);
            
            Evaluator.generateConfusionMatrix(dataset);
            
            Evaluator.printKNNResults(unknownDataset, kNNObject);
            Evaluator.printAccuracy(unknownDataset);
        }
    }
}