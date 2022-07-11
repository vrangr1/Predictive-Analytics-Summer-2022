package TextMining;

import edu.stanford.nlp.simple.*;
import java.util.*;

public class FolderData{

    public List<Document> documents;
    public List<DocumentData> documentDatas;
    public void addDocument(Document doc){
        this.documents.add(doc);
    }

    public void addDocument(DocumentData doc){
        this.documentDatas.add(doc);
    }

    public FolderData(){
        this.documents = new ArrayList<>();
        this.documentDatas = new ArrayList<>();
    }

    public void printFolder(){
        System.out.println("\nFolder Printing Beginning:\n");
        if (this.documentDatas.size() > 0){
            for (DocumentData doc : this.documentDatas)
                doc.printDocument();
        }
        else{
            for (Document doc : this.documents)
                PreProcess.printDocument(doc);
        }
        System.out.println("\nFolder Printing Ends!!\n\n");
    }

    public static void printFolders(List<FolderData> folders){
        for (FolderData folder : folders){
            folder.printFolder();
            break;
        }
    }
}