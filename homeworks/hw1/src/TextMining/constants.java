package TextMining;
import java.util.*;
import java.io.*;

// TODO: Move all this data to a data.txt file!
public class constants{
    // nothing;
    public static final String[] inputFiles = {"/home/anavp/Anav/Acads/NYU/Summer 2022/Predictive-Analytics-Summer-2022/homeworks/hw1/dataset_3/data/C1/article01.txt"};

    public static final String[] inputFolders = {
        "/home/anavp/Anav/Acads/NYU/Summer 2022/Predictive-Analytics-Summer-2022/homeworks/hw1/dataset_3/data/C4", 
        "/home/anavp/Anav/Acads/NYU/Summer 2022/Predictive-Analytics-Summer-2022/homeworks/hw1/dataset_3/data/C1", 
        // "/home/anavp/Anav/Acads/NYU/Summer 2022/Predictive-Analytics-Summer-2022/homeworks/hw1/helper_files/preprocess_testing", 
        "/home/anavp/Anav/Acads/NYU/Summer 2022/Predictive-Analytics-Summer-2022/homeworks/hw1/dataset_3/data/C7"};

    public static final String stopWordsFile = "/home/anavp/Anav/Acads/NYU/Summer 2022/Predictive-Analytics-Summer-2022/homeworks/hw1/helper_files/stopwords.txt"; // source: https://www.link-assistant.com/seo-stop-words.html

    public static List<String> stopWords = new ArrayList<>();

    public static void initialize() throws Exception{
        if (stopWords.size() > 0) return;
        File file = new File(stopWordsFile);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String temp;
        while ((temp = reader.readLine()) != null)
            stopWords.add(temp.replace("\n",""));
    }
}