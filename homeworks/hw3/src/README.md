# CSCI-GA.3033 061(3) Predictive Analytics Homework 3 - Supervised Learning - Follow-up on HW1

## IO Guide
```data.txt```'s first line gives the path of the text file that contains the list of stop words used.  
  
The second line of the same gives the path of the manually written ground truth labels for the test set (unknown0*.txt) given. This is required to generate accuracy on test set.
  
The third line specifies the path of the folder that contains the test files.  
  
Ensuing lines list out the folders of the datasets used for training.  

Output is printed in ```output.txt``` text file.

## Usage

Either use the script given ```script.bash``` to run the program or run the following steps:  
  
To compile, run the following command on terminal in the folder that contains TextMining directory  
```javac TextDataClassifier/Classifier.java```  
  
The code can be run by the following command:  
```java TextDataClassifier.Classifier```

## Testing
Note that testing was done on on a linux based system (ubuntu) with Java version as follows:
```
java -version
openjdk version "1.8.0_312"
OpenJDK Runtime Environment (build 1.8.0_312-8u312-b07-0ubuntu1~18.04-b07)
OpenJDK 64-Bit Server VM (build 25.312-b07, mixed mode)
```

## Comments
In my testing, I observed identical results while experimenting with different values of k used in the k Nearest Algorithms (as can be seen in the output file). I am highly certain that results from lack of proper feature (terms in term-document matrix) filtering that I hoped to have completed in time but couldn't. The last working version of the code (without feature filtering) was, as such, submitted.