package TextDataClassifier;

import java.io.*;
// import java.lang.*;

public class Evaluator{
    private static void writeToFile(int[] truePositives, int[] falsePositives, int[] trueNegatives, int[] falseNegatives) throws Exception{
        // Runtime r = Runtime.getRuntime();
        // r.exec("rm -vf " + constants.topicsFile);
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

    public static void generateConfusionMatrix(Dataset dataset) throws Exception{
    // public static void generateConfusionMatrix(Dataset dataset, int[] docClasses) throws Exception{
        int label, GTLabel, predLabel;
        int n = dataset.folders.size();
        int[] truePositives = new int[n], falsePositives = new int[n], trueNegatives = new int[n], falseNegatives = new int[n];
        kNN.resetZero(truePositives);
        kNN.resetZero(falsePositives);
        kNN.resetZero(trueNegatives);
        kNN.resetZero(falseNegatives);
        for (FolderData folder : dataset.folders){
            GTLabel = folder.folderIndex;
            for (DocumentData doc : folder.documents){
                label = doc.folderIndex;
                // predLabel = docClasses[doc.documentIndex];
                predLabel = doc.generatedLabel;
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

    public static void readTestFilesGroundTruth(Dataset dataset) throws Exception{
        File file = new File(constants.testFilesGroundTruth);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String temp;
        int index = 0;
        while((temp = reader.readLine()) != null){
            // System.out.println("readline: " + temp);
            // System.out.println("char at 0: " + temp.charAt(0));
            dataset.documents.get(index).groundTruth = Character.getNumericValue(temp.charAt(0));
            index += 1;
        }
        reader.close();
    }

    public static void printAccuracy(Dataset dataset) throws Exception{
        double correct = 0;
        for (DocumentData doc : dataset.documents){
            if (doc.groundTruth == doc.generatedLabel) correct += 1;
        }
        double accuracy = (correct / ((double)dataset.documentCount));
        File file = new File(constants.topicsFile);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write("Accuracy on test files = " + accuracy + "\n\n");
        }
    }

    private static void printFuzzyKNNResults(Dataset dataset, kNN kNNObject) throws Exception{
        File file = new File(constants.topicsFile);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            int index = 1;
            for (DocumentData doc : dataset.documents){
                writer.write("unknown");
                if (index < 10) writer.write("0");
                writer.write(index + ".txt was clustered as follows:\n");
                for (int i = 0; i < kNNObject.clusterCount; ++i)
                    writer.write("Cluster " + i + " percentage: " + doc.fuzzykNNResults[i] + "\n");
                writer.write("Ground Truth: " + doc.groundTruth + "\n\n");
                // writer.write("\n");

                index += 1;
            }
        }
    }

    private static void printNonFuzzykNNResults(Dataset dataset, kNN kNNObject) throws Exception{
        File file = new File(constants.topicsFile);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))){
            int index = 1;
            for (DocumentData doc : dataset.documents){
                writer.write("unknown");
                if (index < 10) writer.write("0");
                writer.write(index + ".txt was clustered as: " + doc.generatedLabel + "\n");
                writer.write("Ground Truth: " + doc.groundTruth + "\n\n");
                index += 1;
            }
        }
    }

    public static void printKNNResults(Dataset dataset, kNN kNNObject) throws Exception{
        if (Classifier.toDoFuzzykNN) printFuzzyKNNResults(dataset, kNNObject);
        else printNonFuzzykNNResults(dataset, kNNObject);
    }
}