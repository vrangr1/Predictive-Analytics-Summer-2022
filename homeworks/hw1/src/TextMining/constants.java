package TextMining;
import java.util.*;
import java.io.*;

// TODO: Move all this data to a data.txt file!
public class constants{
    // nothing;
    // public static final String[] inputFiles = {"/home/anavp/Anav/Acads/NYU/Summer 2022/Predictive-Analytics-Summer-2022/homeworks/hw1/dataset_3/data/C1/article01.txt"};
    public static final String dataFile = "data.txt", topicsFile = "topics.txt";
    public static String stopWordsFile;
    public static List<String> inputFolders;
    // public static String[] inputFolders = {
    //     "/home/anavp/Anav/Acads/NYU/Summer 2022/Predictive-Analytics-Summer-2022/homeworks/hw1/helper_files/testing/C4",
    //     "/home/anavp/Anav/Acads/NYU/Summer 2022/Predictive-Analytics-Summer-2022/homeworks/hw1/helper_files/testing/C5",
    //     "/home/anavp/Anav/Acads/NYU/Summer 2022/Predictive-Analytics-Summer-2022/homeworks/hw1/helper_files/testing/C6"
    //     // "/home/anavp/Anav/Acads/NYU/Summer 2022/Predictive-Analytics-Summer-2022/homeworks/hw1/helper_files/preprocess_testing", 
    //     // "/home/anavp/Anav/Acads/NYU/Summer 2022/Predictive-Analytics-Summer-2022/homeworks/hw1/dataset_3/data/C4", 
    //     // "/home/anavp/Anav/Acads/NYU/Summer 2022/Predictive-Analytics-Summer-2022/homeworks/hw1/dataset_3/data/C1", 
    //     // "/home/anavp/Anav/Acads/NYU/Summer 2022/Predictive-Analytics-Summer-2022/homeworks/hw1/dataset_3/data/C7"
    // };

    // public static final String stopWordsFile = "/home/anavp/Anav/Acads/NYU/Summer 2022/Predictive-Analytics-Summer-2022/homeworks/hw1/helper_files/stopwords.txt"; // source: https://www.link-assistant.com/seo-stop-words.html

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
        if (stopWords.size() > 0) return;
        file = new File(stopWordsFile);
        reader = new BufferedReader(new FileReader(file));
        while ((temp = reader.readLine()) != null)
            stopWords.add(temp.replace("\n",""));
        
    }
}