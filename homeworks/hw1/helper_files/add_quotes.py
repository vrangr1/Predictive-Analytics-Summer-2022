import os

if __name__ == '__main__':
    fileName = "stopwords.txt"
    file = open(fileName, 'r')
    file = file.readlines()
    file2 = open("quotedStopWords.txt", 'w')
    for line in file:
        file2.write('\"' + line.strip('\n') + '\"\n')
        # quoted_words.append('"' + line + '"')
    # file.close()
    file2.close()