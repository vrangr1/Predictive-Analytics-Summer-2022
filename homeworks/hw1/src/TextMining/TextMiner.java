package TextMining;

import java.util.*;


public class TextMiner{
    public static void main(String[] args) throws Exception{
        List<FolderData> folders = PreProcess.doPreProcessing();
        FolderData.printFolders(folders);
    }
}