package TextDataClassifier;

import java.util.*;
import java.lang.Math;
// import edu.stanford.nlp.simple.*;

public class TFIDF{

    public Map<String, Integer> indices;
    public List<String> revIndices;
    public int totalWords;
    public List<DocumentData> reverseTFIDFMapping;

    public TFIDF(Dataset dataset){
        indices = new HashMap<String, Integer> ();
        revIndices = new ArrayList<>();
        int index = 0;
        for (String word : dataset.termDocumentFrequency.keySet()){
            revIndices.add(word);
            indices.put(word, index);
            index += 1;
        }
        totalWords = index;

        reverseTFIDFMapping = new ArrayList<>();
    }

    public double[][] doTFIDF(Dataset dataset){
        double[][] tfidfMatrix = new double[dataset.documentCount][totalWords];
        int index = 0, tempIndex;
        double idf;
        for (FolderData folder : dataset.folders){
            for (DocumentData doc : folder.documents){
                for (String word : doc.termCounts.keySet()){
                    tempIndex = indices.get(word);
                    tfidfMatrix[index][tempIndex] = (double)(((double)doc.termCounts.get(word)) / ((double)doc.totalTerms));
                    idf = ((double)dataset.documentCount) / ((double)dataset.termDocumentFrequency.get(word));
                    idf = Math.log(idf);
                    tfidfMatrix[index][tempIndex] *= idf;
                }
                doc.tfidfIndex = index;
                reverseTFIDFMapping.add(doc);
                doc.tfidfVector = tfidfMatrix[index];
                doc.groundTruth = doc.folderIndex;
                index += 1;
            }
        }
        return tfidfMatrix;
    }
}