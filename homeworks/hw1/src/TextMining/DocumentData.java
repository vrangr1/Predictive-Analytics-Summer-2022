package TextMining;

import edu.stanford.nlp.simple.*;
import java.util.*;

public class DocumentData{

    public List<Sentence> sentences;
    public Map<String, Integer> termCounts;
    public int totalTerms;

    private void initializeMap(){
        for (Sentence sent : this.sentences)
            for (String word : sent.words()){
                termCounts.put(word, 0);
            }
        this.totalTerms = this.termCounts.size();
    }

    private void doCounting(){
        for (Sentence sent : this.sentences){
            for (String word : sent.words()){
                termCounts.put(word, termCounts.get(word) + 1);
            }
        }
    }

    public DocumentData(List<Sentence> sentences){
        this.sentences = sentences;
        this.termCounts = new HashMap<String, Integer>();
        this.totalTerms = 0;
        this.initializeMap();
        this.doCounting();
    }

    public void printDocument(){
        System.out.println("\nDocument print begins:\n\n");
        System.out.println("Text:\n");
        for (Sentence sent : this.sentences)
            System.out.println(sent);
        
        System.out.println("\n\nDocument Print Ends\n\n");
    }
}