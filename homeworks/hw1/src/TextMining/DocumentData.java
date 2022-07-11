package TextMining;

import edu.stanford.nlp.simple.*;
import java.util.*;

public class DocumentData{

    public List<Sentence> sentences;
    public Map<String, Integer> termCounts;
    
    public DocumentData(List<Sentence> sentences){
        this.sentences = sentences;
        this.termCounts = new HashMap<String, Integer>();
    }

    public void printDocument(){
        System.out.println("\nDocument print begins:\n\n");
        for (Sentence sent : this.sentences)
            System.out.println(sent);
        System.out.println("\n\nDocument Print Ends\n\n");
    }
}