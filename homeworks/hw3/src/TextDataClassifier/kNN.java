package TextDataClassifier;

import edu.stanford.nlp.simple.*;
import java.util.*;
import java.nio.file.*;
import java.util.stream.Collectors;
public class kNN{

    
    public enum METRIC{
        EUCLIDEAN,
        COSINE
    }

    private static final METRIC metric = METRIC.EUCLIDEAN;
    private double[][] similarityMatrix;
    private static final double knnTrainThreshold = 0.65;
    // private static final double[] knnTrainThreshold = {0, 0, 0.65, 0.65, 0.65, 0.65, 0.65, 0.65, 0.65, 0.65, 0.65, 0.65};
    public int clusterCount = 0;
    private double[] votes;
    private int k;
    
    public kNN(int k){
        this.k = k;
    }

    private static double euclideanDistance(double[] vec1, double[] vec2){
        double ans = 0;
        int len = vec1.length;
        for (int i = 0; i < len; ++i)
            ans += (vec1[i]-vec2[i])*(vec1[i]-vec2[i]);
        return Math.sqrt(ans);
    }

    private static double computeNorm(double[] vec){
        int len = vec.length;
        double ans = 0;
        for (int i = 0; i < len; ++i) ans += vec[i]*vec[i];
        return Math.sqrt(ans);
    }

    private static double cosineSimilarity(double[] vec1, double[] vec2){
        double ans = 0;
        int len = vec1.length;
        for (int i = 0; i < len; ++i) ans += vec1[i] * vec2[i];
        return (ans / (computeNorm(vec1) * computeNorm(vec2)));
    }

    private static double computeDistance(double[] vec1, double[] vec2){
        switch(metric){
            case EUCLIDEAN:
                return euclideanDistance(vec1, vec2);
            case COSINE:
                return cosineSimilarity(vec1, vec2);
        }
        return 0;
    }

    public static void resetZero(double[][] matrix){
        int m = matrix.length, n = matrix[0].length;
        for (int i = 0; i < m; ++i)
            for (int j = 0; j < n; ++j) matrix[i][j] = 0;
    }

    public static void resetZero(int[][] matrix){
        int m = matrix.length, n = matrix[0].length;
        for (int i = 0; i < m; ++i)
            for (int j = 0; j < n; ++j) matrix[i][j] = 0;
    }

    public static void resetZero(double[] vec){
        for (int i = vec.length - 1; i >= 0; --i) vec[i] = 0;
    }

    public static void resetZero(int[] vec){
        for (int i = vec.length - 1; i >= 0; --i) vec[i] = 0;
    }

    public static void addVec(double[] dest, double[] add){
        for (int i = dest.length - 1; i >= 0; --i) dest[i] += add[i];
    }

    public static void divByNum(double[] vec, double num){
        for (int i = vec.length - 1; i >= 0; --i) vec[i] /= num;
    }

    private boolean isClusterZero(String[] keywords){
        for (int i = 0; i < Integer.min(keywords.length, 20);++i){
            if (keywords[i] == "airline") return true;
        }
        return false;
    }

    private boolean isClusterOne(String[] keywords){
        for (int i = 0; i < Integer.min(keywords.length, 20);++i){
            if (keywords[i] == "disease") return true;
        }
        return false;
    }

    private boolean isClusterTwo(String[] keywords){
        for (int i = 0; i < Integer.min(keywords.length, 20);++i){
            if (keywords[i] == "mortgage") return true;
        }
        return false;
    }

    private int getClusterIndex(String[] keywords){
        if (isClusterZero(keywords)) return 0;
        else if (isClusterOne(keywords)) return 1;
        else if (isClusterTwo(keywords)) return 2;
        return this.clusterCount;
    }

    // private static void printGeneratedLabels(Dataset dataset){
    //     for (DocumentData doc: dataset.documents){
    //         System.out.println("Generated Label: " + doc.generatedLabel);
    //     }
    // }

    public void knnTraining(double[][] tfidfMatrix, Dataset dataset) throws Exception{
        int n = tfidfMatrix.length;
        this.similarityMatrix = new double[n][n];
        for (int i = 0; i < n; ++i)
            for (int j = 0; j < n; ++j)
                // similarityMatrix[i][j] = Double.MAX_VALUE;
                this.similarityMatrix[i][j] = 0;
        for (int i = 0; i < n; ++i){
            for (int j = i + 1; j < n; ++j)
            this.similarityMatrix[i][j] = computeDistance(tfidfMatrix[i], tfidfMatrix[j]);
        }
        
        // System.out.println("Printing Similarity Matrix:");
        // Classifier.printMatrix(similarityMatrix);
        this.clusterCount = 0;

        boolean[] clustered = new boolean[n];
        for (int i = 0; i < n; ++i) clustered[i] = false;

        for (int i = 0; i < n; ++i){
            if (clustered[i]) continue;
            String[] keywords = keywordGen.getKeywords(tfidfMatrix[i], dataset);
            dataset.tfidfObject.reverseTFIDFMapping.get(i).generatedLabel = getClusterIndex(keywords);
            for (int j = i + 1; j < n; ++j){
                if (similarityMatrix[i][j] <= knnTrainThreshold){
                    clustered[j] = true;
                    dataset.tfidfObject.reverseTFIDFMapping.get(j).generatedLabel = dataset.tfidfObject.reverseTFIDFMapping.get(i).generatedLabel;
                }
            }
            this.clusterCount += 1;
        }

        System.out.println("Cluster Count: " + this.clusterCount);
        // System.out.println("Generated Labels:");
        // printGeneratedLabels(dataset);
        System.out.print("\n");
    }

    private void vote(int label, boolean reset){
        if (reset){
            this.votes = new double[clusterCount];
            resetZero(votes);
        }
        this.votes[label] += 1.0;
    }

    private int getLabel(){
        // if (Classifier.toDoFuzzykNN)
        int label = 0;
        double recVotes;
        recVotes = votes[label];
        for (int i = 1; i < clusterCount; ++i){
            if (recVotes < votes[i]){
                label = i;
                recVotes = votes[i];
            }
        }
        return label;
    }

    private double[] getFuzzyLabel(){
        double sum = 0;
        for (int i = 0; i < clusterCount; ++i)
            sum += votes[i];
        double[] labels = new double[clusterCount];
        for (int i = 0; i < clusterCount; ++i){
            // System.out.println("votes[i]: " + votes[i]);
            // System.out.println("sum: " + sum);
            labels[i] = (votes[i]/sum)*((double)100);
            // System.out.println("labels[i]: " + labels[i]);
        }
        return labels;
    }

    private void dokNN(double[] tfidfVector, double[][] tfidfMatrix, TFIDF tfidfObject){
        int n = tfidfMatrix.length;
        double[][] distances = new double[n][2];
        resetZero(distances);
        for (int i = 0; i < n; ++i){
            distances[i][0] = computeDistance(tfidfVector, tfidfMatrix[i]);
            distances[i][1] = i;
        }
        Arrays.sort(distances, new java.util.Comparator<double[]>() {
            public int compare(double[] a, double[] b) {
                return Double.compare(a[0], b[0]);
            }
        });

        // System.out.println("Distances: ");
        // Classifier.printMatrix(distances);

        vote(tfidfObject.reverseTFIDFMapping.get((int)distances[0][1]).generatedLabel, true);

        for (int i = 1; i < k; ++i)
            vote(tfidfObject.reverseTFIDFMapping.get((int)distances[i][1]).generatedLabel, false);
        
        // System.out.println("After voting:");
        // Classifier.printVec(votes);
        // return getLabel();
    }

    public int knnTestingOneDocument(String filePath, double[][]tfidfMatrix, TFIDF tfidfObject) throws Exception{
        Document doc;
        DocumentData docData;
        FolderData unknown = new FolderData();
        Dataset unknownDataset = new Dataset();
        doc = new Document(inputIO.readFile(filePath));
        docData = new DocumentData(PreProcess.preProcessDocument(doc));
        unknown.addDocument(docData);
        unknownDataset.addFolder(unknown);
        unknownDataset.doTermDocumentFrequencyEvaluation();
        unknownDataset.tfidfObject = new TFIDF(unknownDataset);
        double[][] unknownTFIDFMatrix = unknownDataset.tfidfObject.doTFIDF(unknownDataset);
        dokNN(unknownTFIDFMatrix[0], tfidfMatrix, tfidfObject);
        return getLabel();
    }

    public Dataset knnTesting(String folderPath, double[][] tfidfMatrix, TFIDF tfidfObject) throws Exception{
        Document doc;
        DocumentData docData;
        FolderData unknown = new FolderData();
        Dataset unknownDataset = new Dataset();
        List<String> filePaths = Files.walk(Paths.get(folderPath)).filter(Files::isRegularFile).map(Path::toAbsolutePath).map(Object::toString).collect(Collectors.toList());
        Collections.sort(filePaths);
        for (String path : filePaths){
            System.out.println("Unknown path: " + path);
            doc = new Document(inputIO.readFile(path));
            docData = new DocumentData(PreProcess.preProcessDocument(doc));
            unknown.addDocument(docData);
        }
        unknownDataset.addFolder(unknown);
        unknownDataset.doTermDocumentFrequencyEvaluation();
        unknownDataset.tfidfObject = new TFIDF(unknownDataset);
        double[][] unknownTFIDFMatrix = unknownDataset.tfidfObject.doTFIDF(unknownDataset);
        int n = unknownTFIDFMatrix.length;
        for (int i = 0; i < n; ++i){
            dokNN(unknownTFIDFMatrix[i], tfidfMatrix, tfidfObject);
            if (Classifier.toDoFuzzykNN)
                unknownDataset.tfidfObject.reverseTFIDFMapping.get(i).fuzzykNNResults = getFuzzyLabel();
            else
                unknownDataset.tfidfObject.reverseTFIDFMapping.get(i).generatedLabel = getLabel();
        }
        
        return unknownDataset;
    }
}