import pickle

fileOut = "C:\\Users\Carl Wilhjelm\PycharmProjects\PharmaPal\dictDatabaseFileObj.txt"

databaseDict = {}

databaseDict['Excedrin'] = "Aspirin/paracetamol/caffeine is a combination drug for the treatment of pain, especially tension headache and migraine."
databaseDict['Aspirin'] = "It can treat pain, fever, headache, and inflammation. It can also reduce the risk of heart attack."
databaseDict['Omeprazole'] = "It can treat heartburn, a damaged esophagus, stomach ulcers, and gastroesophageal reflux disease (GERD)."




with open(fileOut, 'wb') as f:
    pickle.dump(databaseDict, f)