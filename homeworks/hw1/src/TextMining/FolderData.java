package TextMining;

import edu.stanford.nlp.simple.*;
import java.util.*;

public class FolderData{

    public List<DocumentData> documents;
    public Map<String, Integer> termDocumentFrequency;

    public void addDocument(DocumentData doc){
        this.documents.add(doc);
    }

    public FolderData(){
        this.documents = new ArrayList<>();
        this.termDocumentFrequency = new HashMap<String, Integer>();
    }
    
    public void doTermDocumentFrequencyEvaluation(){
        for (DocumentData doc : this.documents){
            for (String word : doc.termCounts.keySet()){
                this.termDocumentFrequency.put(word, 0);
            }
            // for(Sentence sent : doc.sentences){
            //     for (String word : sent.words()){
            //         this.termDocumentFrequency.put(word, 0);
            //     }
            // }
        }

        for (String word : this.termDocumentFrequency.keySet()){
            for (DocumentData doc : this.documents){
                if (doc.termCounts.containsKey(word))
                    this.termDocumentFrequency.put(word, this.termDocumentFrequency.get(word) + 1);
            }
        }
    }

    public void printFolder(){
        System.out.println("\nFolder Printing Beginning:\n");
        for (DocumentData doc : this.documents)
            doc.printDocument();
        System.out.println("\nFolder Printing Ends!!\n\n");
    }

    public static void printFolders(List<FolderData> folders){
        for (FolderData folder : folders){
            folder.printFolder();
            break;
        }
    }
}