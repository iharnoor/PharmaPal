import cv2
import distance
import os
from PIL import Image
import pickle
import pytesseract
import nltk

imageFileIn = "C:\\Users\Carl Wilhjelm\PycharmProjects\PharmaPal\excedrineTest4.jpg"
grayImageFileOut = "C:\\Users\Carl Wilhjelm\PycharmProjects\PharmaPal\excedrineTest4.png"
dictDatabaseFileIn = "C:\\Users\Carl Wilhjelm\PycharmProjects\PharmaPal\dictDatabaseFileObj.txt"

with open(dictDatabaseFileIn, 'rb') as f:
    dictDatabase = pickle.load(f)

# def setImageFile(pathIn):
#     imageFileIn = pathIn
#     grayImageFileOut = pathIn[:-4] + ".gray.png"
#     return

# find all words in original image
rawText = pytesseract.image_to_string(Image.open(imageFileIn))
tokens = nltk.word_tokenize(rawText)
# print(nltk.pos_tag(tokens))


# find all words in gray image
image = cv2.imread(imageFileIn)
gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
gray = cv2.threshold(gray, 0, 255, cv2.THRESH_BINARY | cv2.THRESH_OTSU)[1]
gray = cv2.medianBlur(gray, 3)
grayImageOut = grayImageFileOut.format(os.getpid())
cv2.imwrite(grayImageOut, gray)
processedRawText = pytesseract.image_to_string(Image.open(grayImageOut))
processedTokens = nltk.word_tokenize(processedRawText)
# print(nltk.pos_tag(processedTokens))

dictResults = {}
for key in dictDatabase:
    keyLower = key.lower()
    for s in tokens:
        if distance.levenshtein(s.lower(), keyLower) <= 2:
            dictResults[key] = dictDatabase[key]
    for s in processedTokens:
        if distance.levenshtein(s.lower(), keyLower) <= 2:
            dictResults[key] = dictDatabase[key]

for key in dictResults:
    print(key + ": " + dictResults[key])