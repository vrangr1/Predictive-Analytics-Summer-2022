package TextDataClassifier;

import edu.stanford.nlp.simple.*;
import java.util.*;
import java.nio.file.*;
import java.util.stream.Collectors;

public class PreProcess{

    public static boolean debug_mode = Classifier.debugMode;

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
        for (String word : words){
            boolean found = false;
            for (String stopWord : constants.stopWords){
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

        for (int i = 0; i < n; ++i){
            Sentence sent = stopWordsRemoval(sentences.get(i));
            if (sent != null) sentences.set(i, sent);
            else delIndices.push(i);
        }

        while(!delIndices.empty())
            sentences.remove((int)delIndices.pop());
        
        if (debug_mode){
            System.out.println("Stop Words Removed!!!");
            printSentences(sentences);
        }
        
        for (int i = 0; i < n; ++i) 
            if (sentences.get(i) != null) sentences.set(i, new Sentence(sentences.get(i).lemmas()));
        
        if (debug_mode){
            System.out.println("After Lemmatization!!!");
            printSentences(sentences);
        }

        for (int i = 0; i < n; ++i)
            sentences.set(i, doNER(sentences.get(i)));
        // sentences.set(i, new Sentence(sentences.get(i).nerTags()));
        
        if (debug_mode){
            System.out.println("After NERTags Generation");
            printSentences(sentences);
        }
        return sentences;
    }

    // public static void doPreProcessing() throws Exception{
    public static Dataset doPreProcessing() throws Exception{
        if (constants.stopWords.size() == 0) constants.initialize();
        Dataset dataset = new Dataset();
        FolderData folder;
        Document doc;
        DocumentData docData;
        int index = 0, fInd = 0;

        for (String folderName : constants.inputFolders){
            List<String> fileNames = Files.walk(Paths.get(folderName)).filter(Files::isRegularFile).map(Path::toAbsolutePath).map(Object::toString).collect(Collectors.toList());
            folder = new FolderData(index, fInd);
            fInd += 1;
            for (String fileName : fileNames){
                System.out.println("fileName: " + fileName);
                doc = new Document(inputIO.readFile(fileName));
                docData = new DocumentData(preProcessDocument(doc));
                folder.addDocument(docData);
                index += 1;
            }
            dataset.addFolder(folder);
        }
        dataset.doTermDocumentFrequencyEvaluation();
        return dataset;
    }
}