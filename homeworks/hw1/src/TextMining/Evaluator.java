package TextMining;

import java.util.*;

public class Evaluator{
    public static void generateConfusionMatrix(Dataset dataset, int[] docClasses){
        // int[][] confusionMatrix = new int[dataset.documentCount][dataset.documentCount];
        int n = dataset.documentCount;
        
        int[][] truePositives = new int[n][n], falsePositives = new int[n][n], trueNegatives = new int[n][n], falseNegatives = new int[n][n];
        kMeans.resetZero(truePositives);
        kMeans.resetZero(falsePositives);
        kMeans.resetZero(trueNegatives);
        kMeans.resetZero(falseNegatives);

        // for label in training_data.labels_list:
        //         counts.true_positives[label] = counts.true_positives.get(label, 0)
        //         if label == label_gt and label_gt == best_label:
        //             counts.true_positives[label] += 1
        //         elif label == label_gt and label_gt != best_label:
        //             counts.false_negatives[label] = counts.false_negatives.get(label, 0) + 1
        //         elif best_label == label:
        //             counts.false_positives[label] = counts.false_positives.get(label, 0) + 1
        //         else: # elif best_label == label_gt:
        //             counts.true_negatives[label] = counts.true_negatives.get(label, 0) + 1
        // int label, GTLabel, predLabel;
        // for (DocumentData doc1 : dataset.documents){
        //     GTLabel = doc1.folderIndex;
        //     predLabel = docClasses[doc1.documentIndex];
        //     for (DocumentData doc2 : dataset.documents){
        //         label = doc2.folderIndex;
        //         if (label == GTLabel && GTLabel == predLabel){
        //             truePositives[]
        //         }
        //     }
        // }
    }
}