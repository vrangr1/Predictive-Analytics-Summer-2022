package TextMining;

import edu.stanford.nlp.simple.*;
import java.util.*;
import java.lang.Math;

public class kMeans{
    public enum METRIC{
        EUCLIDEAN,
        COSINE
    }

    private static int documentCount, totalWords, k, iterations;
    private static METRIC chosenMetric;
    private static final int ITERATION_LIMIT = 1000;
    private static final boolean doKMeansPlusPlus = true;


    private static void copyOver(double[] dest, double[] og){
        for (int i = dest.length - 1; i >= 0; --i) dest[i] = og[i];
    }

    private static double[][] initializeCentroidsByKMeansPlusPlus(double[][] tfidfMatrix){
        double[][] centroids = new double[k][totalWords];
        Random rd = new Random();
        int last = rd.nextInt(documentCount);
        copyOver(centroids[0], tfidfMatrix[last]);
        double maxDist, tempDist = 0, guess;
        int newInd;
        for (int i = 1; i < k; ++i){
            newInd = 0;
            maxDist = computeDistance(centroids[i-1], tfidfMatrix[0]);
            for (int j = 1; j < documentCount; ++j){
                tempDist = computeDistance(centroids[i-1], tfidfMatrix[j]);
                if (tempDist > maxDist)
                    maxDist = tempDist;
            }
            guess = rd.nextInt((int)maxDist);
            for (int j = 0; j < documentCount; ++j){
                tempDist = computeDistance(centroids[i-1], tfidfMatrix[j]);
                if (tempDist > guess) continue;
                newInd = j;
                break;
            }
            maxDist = tempDist;
            for (int j = newInd + 1; j < documentCount; ++j){
                tempDist = computeDistance(centroids[i-1], tfidfMatrix[j]);
                if (tempDist > guess) continue;
                if (tempDist > maxDist){
                    maxDist = tempDist;
                    newInd = j;
                }
            }
            copyOver(centroids[i], tfidfMatrix[newInd]);
        }
        return centroids;
    }

    private static double[][] initializeCentroids(double[][] tfidfMatrix){
        if (doKMeansPlusPlus) return initializeCentroidsByKMeansPlusPlus(tfidfMatrix);
        double[][] centroids = new double[k][totalWords];
        Random rd = new Random();
        for (int i = 0; i < k; ++i)
            for (int j = 0; j < totalWords; ++j)
                centroids[i][j] = rd.nextDouble();
        return centroids;
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
        switch(chosenMetric){
            case EUCLIDEAN:
                return euclideanDistance(vec1, vec2);
            case COSINE:
                return cosineSimilarity(vec1, vec2);
        }
        return 0;
    }

    private static boolean classificationIteration(double[][] tfidfMatrix, double[][]centroids, int[] docClasses){
        if (iterations > ITERATION_LIMIT) return false;
        boolean update = false;
        double minDist, tempDist;
        int index;
        for (int i = 0; i < documentCount; ++i){
            minDist = computeDistance(tfidfMatrix[i], centroids[0]);
            index = 0;
            for (int j = 1; j < k; ++j){
                tempDist = computeDistance(tfidfMatrix[i], centroids[j]);
                if (minDist > tempDist){
                    minDist = tempDist;
                    index = j;
                }
            }
            if (index != docClasses[i]) update = true;
            docClasses[i] = index;
        }
        iterations += 1;
        return update;
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

    private static void divByNum(double[] vec, double num){
        for (int i = vec.length - 1; i >= 0; --i) vec[i] /= num;
    }

    private static void updateCentroids(double[][] tfidfMatrix, double[][] centroids, int[] docClasses){
        resetZero(centroids);
        double[] counts = new double[k];
        resetZero(counts);
        for (int i = 0; i < documentCount; ++i){
            counts[docClasses[i]] += 1;
            addVec(centroids[docClasses[i]], tfidfMatrix[i]);
        }
        for (int i = 0; i < k; ++i) divByNum(centroids[i], counts[i]);
    }

    public static int[] doKMeans(double[][] tfidfMatrix, int k, METRIC metric){
        documentCount = tfidfMatrix.length;
        totalWords = tfidfMatrix[0].length;
        chosenMetric = metric;
        kMeans.k = k;
        iterations = 0;
        int[] docClasses = new int[documentCount];
        
        double[][] centroids = initializeCentroids(tfidfMatrix);
        
        for (int i = 0; i < documentCount; ++i) docClasses[i] = i%k;
        
        boolean update = true, repeat = false;
        while(update || repeat){
            update = classificationIteration(tfidfMatrix, centroids, docClasses);
            if (update) updateCentroids(tfidfMatrix, centroids, docClasses);
            else if (repeat) break;
            else repeat = true;
        }
        return docClasses;
    }
}