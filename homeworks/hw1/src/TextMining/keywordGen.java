package TextMining;

import java.util.*;
import edu.stanford.nlp.simple.*;

// class Pair implements Comparable<Pair> {
//     public final int index;
//     public final double value;

//     public Pair(int index, double value) {
//         this.index = index;
//         this.value = value;
//     }

//     @Override
//     public int compareTo(Pair other) {
//         return Integer.valueOf(this.value).compareTo(other.value);
//     }
// }

public class keywordGen{

    private static void printArr(double[] vec, Integer[] sortedInds){
        System.out.println("Original Array: ");
        for (int i = 0; i < vec.length; ++i){
            System.out.print(vec[i]);
            if (i < vec.length - 1) System.out.print(", ");
        }
        System.out.println("\nog array ends\n");
        System.out.println("Sorted Array:");
        for (int i = 0 ;i < vec.length; ++i){
            System.out.print(vec[sortedInds[i]]);
            if (i < vec.length - 1) System.out.print(", ");
        }
        System.out.println("\nprinting ends\n");
    }

    private static String[] getKeywords(double[] vec){
        int len = vec.length;
        Integer[] sortedIndices = new Integer[len];
        for (int i = 0; i < len; ++i) sortedIndices[i] = i;
        Arrays.sort(sortedIndices, new Comparator<Integer>(){
            public int compare(Integer ind1, Integer ind2){
                return -1*Double.compare(vec[ind1], vec[ind2]);
            }
        });
        String[] keywords = new String[len];
        if (TextMiner.debugMode)
            printArr(vec, sortedIndices);
        for (int i = 0; i < len; ++i){
            keywords[i] = TFIDF.revIndices.get(sortedIndices[i]);
        }
        return keywords;
    }

    public static void generateKeywords(double[][] tfidfMatrix, Dataset dataset){
        int index = 0;
        for (FolderData folder : dataset.folders){
            double[] vec = new double[tfidfMatrix[0].length];
            kMeans.resetZero(vec);
            for (DocumentData doc : folder.documents)
                kMeans.addVec(vec, tfidfMatrix[doc.documentIndex]);
            String[] keywords = getKeywords(vec);
            System.out.println("Keywords for folder " + index + ":");
            for (int i = 0; i < keywords.length; ++i){
                System.out.print(keywords[i]);
                if (i < keywords.length - 1) System.out.print(", ");
            }
            System.out.println("\n");
            index += 1;
        }
    }
}