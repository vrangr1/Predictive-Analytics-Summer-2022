package TextMining;
import java.util.*;
import java.io.*;

public class constants{
    public static final String dataFile = "data.txt", topicsFile = "topics.txt";
    public static String stopWordsFile;
    public static List<String> inputFolders;
    
    public static List<String> stopWords = new ArrayList<>();

    public static void initialize() throws Exception{
        File file = new File(dataFile);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String temp;
        boolean first = true;
        inputFolders = new ArrayList<>();
        while((temp = reader.readLine()) != null){
            if (first){
                first = false;
                stopWordsFile = temp.replace("\n","");
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