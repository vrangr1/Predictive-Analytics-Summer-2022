package TextMining;

import edu.stanford.nlp.simple.*;
import java.util.*;
import java.nio.file.*;
import java.io.File;
import java.util.stream.Collectors;

public class PreProcess extends inputIO{

    public static final boolean debug_mode = false;

    public static void printDocument(Document doc){
        System.out.println("\nDocument Starts here:\n\n");
        for (Sentence sent : doc.sentences())
            System.out.println(sent);
        System.out.println("\n\nDocument Ends here\n\n");
    }

    public static void printSentences(List<Sentence> sentences){
        System.out.println("\nSentences printing beginning:\n");
        for (Sentence sent : sentences)
            System.out.println("sentence: " + sent + ";;;;;;; sentence finished\n");
        System.out.println("\nSentences printing finished!\n");
    }

    public static Sentence stopWordsRemoval(Sentence sent){
        List<String> words = sent.words();
        // TODO: Stop Words removal being done by iterating through the entire stop words list to find whether the current word is a stop word or not. Optimize by using hashmaps or tries.
        List<String> newWords = new ArrayList<>();
        // System.out.println("words count: " + words.size());
        // System.out.println(sent + "\n\n");
        for (String word : words){
            boolean found = false;
            for (String stopWord : stopWords){
                if (word.toLowerCase().equals(stopWord)) found = true;
                if (found) break;
            }
            if (!found) newWords.add(word);
        }
        if (newWords.size() > 0)
        return new Sentence(newWords);
        return null;
    }

    public static Sentence doNER(Sentence sent){
        List<String> tags = sent.nerTags(), words = new ArrayList<>(), ogWords = sent.words();
        int n = tags.size();
        boolean begun = false;
        String word = "", tag = "";
        // if (debug_mode){
        //     System.out.println("NERTAGS!!!!!");
        //     System.out.println("og: " + sent);
        //     System.out.println("tags: " + tags);
        // }
        for (int i = 0; i < n; ++i){
            if (tags.get(i).equals("O")){
                if (begun){
                    words.add(word.toLowerCase());
                    begun = false;
                    word = "";
                }
                words.add(ogWords.get(i).toLowerCase());
                continue;
            }
            if (!begun){
                begun = true;
                word = ogWords.get(i);
                tag = tags.get(i);
                continue;
            }
            if (tag.equals(tags.get(i)))
            word = word + "_" + ogWords.get(i);
            else{
                words.add(word.toLowerCase());
                word = ogWords.get(i);
                tag = tags.get(i);
            }
        }
        if (begun) words.add(word.toLowerCase());
        return new Sentence(words);
    }

    public static List<Sentence> preProcessDocument(Document doc){
        List<Sentence> sentences = doc.sentences();
        int n = sentences.size();

        if (debug_mode)
        printSentences(sentences);

        Stack<Integer> delIndices = new Stack<Integer>();

        // Stop Words Removal:
        for (int i = 0; i < n; ++i){
            Sentence sent = stopWordsRemoval(sentences.get(i));
            if (sent != null) sentences.set(i, sent);
            else delIndices.push(i);
        }

        // Remove Null Sentences:
        while(!delIndices.empty())
            sentences.remove((Integer)delIndices.pop());
        
        if (debug_mode){
            System.out.println("Stop Words Removed!!!");
            printSentences(sentences);
        }
        
        // TODO: Do the same delIndices thing for the next 3 steps:

        // Tokenization and Lemmatization;
        for (int i = 0; i < n; ++i) 
            if (sentences.get(i) != null) sentences.set(i, new Sentence(sentences.get(i).lemmas()));
        
        if (debug_mode){
            System.out.println("After Lemmatization!!!");
            printSentences(sentences);
        }

        // Named Entity Recognition
        for (int i = 0; i < n; ++i)
            sentences.set(i, doNER(sentences.get(i)));
        // sentences.set(i, new Sentence(sentences.get(i).nerTags()));
        
        if (debug_mode){
            System.out.println("After NERTags Generation");
            printSentences(sentences);
        }
        
        // TODO: Step 4: Sliding Window approach to group common 2/3 grams

        return sentences;
    }

    // public static void doPreProcessing() throws Exception{
    public static Dataset doPreProcessing() throws Exception{
        if (stopWords.size() == 0) initialize();
        Dataset dataset = new Dataset();
        FolderData folder;
        Document doc;
        DocumentData docData;

        for (String folderName : inputFolders){
            List<File> files = Files.walk(Paths.get(folderName)).filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList());
            List<String> fileNames = Files.walk(Paths.get(folderName)).filter(Files::isRegularFile).map(Path::toAbsolutePath).map(Object::toString).collect(Collectors.toList());
            folder = new FolderData();
            for (String fileName : fileNames){
                System.out.println("fileName: " + fileName);
                doc = new Document(readFile(fileName));
                docData = new DocumentData(preProcessDocument(doc));
                folder.addDocument(docData);
            }
            dataset.addFolder(folder);
        }
        dataset.doTermDocumentFrequencyEvaluation();
        return dataset;
    }
}