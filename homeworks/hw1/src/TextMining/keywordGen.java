package TextMining;

import java.util.*;
import java.io.*;

public class keywordGen{
    private static boolean appendToFile;
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

    private static int minFunc(int num1, int num2){
        if (num1 < num2) return num1;
        return num2;
    }

    private static void printKeywords(String[] keywords, int index) throws Exception{
        File file = new File(constants.topicsFile);
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, appendToFile))) 
        {
            writer.write("Keywords for folder: " + index + ": \n");
            for (int i = 0; i < minFunc(keywords.length, 10); ++i){
                writer.write(keywords[i]);
                if (i < keywords.length - 1) writer.write(", ");
            }
            writer.write("\n\n");
        }
        if (!appendToFile) appendToFile = true;

    }

    public static void generateKeywords(double[][] tfidfMatrix, Dataset dataset) throws Exception{
        int index = 0;
        appendToFile = false;
        for (FolderData folder : dataset.folders){
            double[] vec = new double[tfidfMatrix[0].length];
            kMeans.resetZero(vec);
            for (DocumentData doc : folder.documents)
                kMeans.addVec(vec, tfidfMatrix[doc.documentIndex]);
            String[] keywords = getKeywords(vec);

            printKeywords(keywords, index);
            index += 1;
        }
    }
}