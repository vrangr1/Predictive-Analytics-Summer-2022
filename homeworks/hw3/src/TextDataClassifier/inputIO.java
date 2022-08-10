package TextDataClassifier;

import java.io.File;
import java.util.Scanner;

public class inputIO{
    public static String readFile(String fileName) throws Exception{
        File file = new File(fileName);
        String ans;
        Scanner sc = new Scanner(file);
        sc.useDelimiter("\\Z");
        ans = sc.next();
        sc.close();
        return ans;
    }

    // public static void writeToTopics()
}