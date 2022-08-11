package TextDataClassifier;

import java.util.*;
import java.io.*;

public class constants{
    public static final String dataFile = "data.txt", topicsFile = "output.txt";
    public static String testFilesGroundTruth;
    public static String stopWordsFile;
    public static String unknownFolderPath;
    public static List<String> inputFolders;
    public static List<String> stopWords = new ArrayList<>();

    public static void initialize() throws Exception{
        File file = new File(dataFile);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String temp;
        boolean first = true, second = false, third = false;
        inputFolders = new ArrayList<>();
        while((temp = reader.readLine()) != null){
            if (first){
                first = false;
                stopWordsFile = temp.replace("\n","");
                second = true;
                continue;
            }
            if (second){
                second = false;
                testFilesGroundTruth = temp.replace("\n","");
                third = true;
                continue;
            }
            if (third){
                third = false;
                unknownFolderPath = temp.replace("\n", "");
                continue;
            }
            inputFolders.add(temp.replace("\n",""));
        }
        reader.close();
        if (stopWords.size() > 0) return;
        file = new File(stopWordsFile);
        reader = new BufferedReader(new FileReader(file));
        while ((temp = reader.readLine()) != null)
            stopWords.add(temp.replace("\n",""));
        reader.close();
    }
}