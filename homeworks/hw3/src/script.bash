#!/bin/bash

cd TextDataClassifier
rm -fv *.class
cd ..
javac TextDataClassifier/Classifier.java
java TextDataClassifier.Classifier
