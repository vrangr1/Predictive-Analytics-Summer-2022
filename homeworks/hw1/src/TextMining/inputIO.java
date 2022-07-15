package TextMining;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class inputIO{
    public static String readFile(String fileName) throws Exception{
        File file = new File(fileName);
        String ans;
        Scanner sc = new Scanner(file);
        sc.useDelimiter("\\Z");
        ans = sc.next();
        // System.out.println(ans);
        return ans;
    }
}