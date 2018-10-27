import pickle

fileOut = "C:\\Users\Carl Wilhjelm\PycharmProjects\PharmaPal\dictDatabaseFileObj.txt"

databaseDict = {}

databaseDict['Excedrin'] = "Aspirin/paracetamol/caffeine is a combination drug for the treatment of pain, especially tension headache and migraine."
databaseDict['Aspirin'] = "It can treat pain, fever, headache, and inflammation. It can also reduce the risk of heart attack."
databaseDict['Omeprazole'] = "It can treat heartburn, a damaged esophagus, stomach ulcers, and gastroesophageal reflux disease (GERD)."
databaseDict['Meloxicam'] = "Nonsteroidal anti-Inflammatory drug. It can treat osteoarthritis (OA) and rheumatoid arthritis (RA)."
databaseDict['Benadryl'] = "Antihistamine. It can treat pain and itching caused by insect bites, minor cuts, burns, poison ivy, poison oak, and poison sumac when applied topically. In its oral form, it can treat hay fever, allergies, cold symptoms, and insomnia."
databaseDict['Cyclobenzaprine'] = "Muscle relaxant It can treat pain and stiffness caused by muscle spasms."
databaseDict['Lidocaine'] = "Anesthetic and antiarrhythmic. It can treat irregular heartbeats (arrhythmias). It can also relieve pain and numb the skin."
databaseDict['Alka-'] = "Sodium bicarbonate (Alka-Seltzer Heartburn Relief) is a drug used to treat excess stomach acid and heartburn."

with open(fileOut, 'wb') as f:
    pickle.dump(databaseDict, f)