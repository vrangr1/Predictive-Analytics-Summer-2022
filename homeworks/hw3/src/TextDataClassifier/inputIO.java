package TextDataClassifier;

import java.nio.file.*;

public class inputIO{
    public static String readFile(String fileName) throws Exception{
        String data = "";
        data = new String(Files.readAllBytes(Paths.get(fileName)));
        return data;
    }
}