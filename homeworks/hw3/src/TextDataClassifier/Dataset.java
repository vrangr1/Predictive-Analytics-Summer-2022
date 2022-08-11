package TextDataClassifier;

import java.util.*;

public class Dataset{
    public List<FolderData> folders;
    public List<DocumentData> documents;
    public int documentCount;
    public Map<String, Integer> termDocumentFrequency;

    public TFIDF tfidfObject;

    public Dataset(){
        this.folders = new ArrayList<>();
        this.documentCount = 0;
        this.termDocumentFrequency = new HashMap<String, Integer>();
        this.documents = new ArrayList<>();
    }

    public void doTermDocumentFrequencyEvaluation(){
        for (FolderData folder : this.folders){
            for (String word : folder.termDocumentFrequency.keySet()){
                this.termDocumentFrequency.put(word, 0);
            }
        }
        for (FolderData folder: this.folders){
            for (Map.Entry<String, Integer> entry : folder.termDocumentFrequency.entrySet()){
                this.termDocumentFrequency.put(entry.getKey(), this.termDocumentFrequency.get(entry.getKey()) + entry.getValue());
            }
        }
    }

    public Dataset(List<FolderData> folders){
        this.folders = folders;
        this.documentCount = 0;
        for (FolderData folder : this.folders)
            this.documentCount += folder.documents.size();
        this.termDocumentFrequency = new HashMap<String, Integer>();
        this.doTermDocumentFrequencyEvaluation();
    }

    public void addFolder(FolderData folder){
        folder.doTermDocumentFrequencyEvaluation();
        this.folders.add(folder);
        this.documentCount += folder.documents.size();
        for (DocumentData doc : folder.documents){
            this.documents.add(doc);
        }
    }

    public void printDataset(){
        System.out.println("Dataset printing begins:\n");
        FolderData.printFolders(folders);

        System.out.println("Printing term document Frequency");
        for (Map.Entry<String, Integer> entry : termDocumentFrequency.entrySet()){
            System.out.println("word: " + entry.getKey() + "; count: " + entry.getValue());
        }
        System.out.println("Print of term document frequency ends\n");
        System.out.println("Dataset Printing Ends\n");
    }
}