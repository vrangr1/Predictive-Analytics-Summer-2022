package TextMining;

import java.io.*;

public class Evaluator{
    private static void writeToFile(int[] truePositives, int[] falsePositives, int[] trueNegatives, int[] falseNegatives) throws Exception{
        File file = new File(constants.topicsFile);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) 
        {
            for (int i = 0; i < truePositives.length; ++i){
                writer.write("Confusion Matrix for folder: " + i + ": \n\n");
                writer.write("True Positives: " + truePositives[i] + "\n");
                writer.write("True Negatives: " + trueNegatives[i] + "\n");
                writer.write("False Positives: " + falsePositives[i] + "\n");
                writer.write("False Negatives: " + falseNegatives[i] + "\n\n\n");
            }
        }
    }

    private static void computePrecisionRecallF1ScoreAndWrite(int[] truePositives, int[] falsePositives, int[] trueNegatives, int[] falseNegatives) throws Exception{
        double precision = 0, recall = 0, f1Score = 0, tp = 0, fp = 0, fn = 0;
        for (int i = truePositives.length - 1; i >= 0; --i){
            tp += truePositives[i];
            fp += falsePositives[i];
            fn += falseNegatives[i];
        }
        precision = (tp/(tp+fp));
        recall = (tp/(tp+fn));
        f1Score = (((double)2*precision * recall)/ (precision + recall));
        File file = new File(constants.topicsFile);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write("Precision: " + precision + "\n\n");
            writer.write("Recall: " + recall + "\n\n");
            writer.write("F1Score: " + f1Score + "\n\n");
        }
    }

    public static void generateConfusionMatrix(Dataset dataset, int[] docClasses) throws Exception{
        int label, GTLabel, predLabel;
        int n = dataset.folders.size();
        int[] truePositives = new int[n], falsePositives = new int[n], trueNegatives = new int[n], falseNegatives = new int[n];
        kMeans.resetZero(truePositives);
        kMeans.resetZero(falsePositives);
        kMeans.resetZero(trueNegatives);
        kMeans.resetZero(falseNegatives);
        for (FolderData folder : dataset.folders){
            GTLabel = folder.folderIndex;
            for (DocumentData doc : folder.documents){
                label = doc.folderIndex;
                predLabel = docClasses[doc.documentIndex];
                if (label == GTLabel && GTLabel == predLabel)
                    truePositives[label] += 1;
                else if (label == GTLabel && GTLabel != predLabel)
                    falseNegatives[label] += 1;
                else if (predLabel == label)
                    falsePositives[label] += 1;
                else // predLabel == GTLabel
                    trueNegatives[label] += 1;
            }
        }
        writeToFile(truePositives, falsePositives, trueNegatives, falseNegatives);
        computePrecisionRecallF1ScoreAndWrite(truePositives, falsePositives, trueNegatives, falseNegatives);
    }
}