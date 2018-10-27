import pickle

fileOut = "dictDatabaseFileObj.txt"

# langEnumerator = {'eng': 0, 'hin': 2, 'chi': 3, 'spn': 4}

databaseDict = {}

databaseDict['Excedrin'] = ["\"Excedrin\": Aspirin/paracetamol/caffeine is a combination drug for the treatment of pain, especially tension headache and migraine.",
                            "\"एक्सेड्रिन\": एस्पिरिन / पैरासिटामोल / कैफीन दर्द के इलाज के लिए एक संयोजन दवा है, विशेष रूप से तनाव सिरदर्द और माइग्रेन।",
                            "\"Excedrin\"：阿司匹林/对乙酰氨基酚/咖啡因是一种用于治疗疼痛，特别是紧张性头痛和偏头痛的组合药物。",
                            "\"Excedrin\": la aspirina / paracetamol / cafeína es un medicamento combinado para el tratamiento del dolor, especialmente el dolor de cabeza por tensión y la migraña."
                            ]

databaseDict['Meloxicam'] = ["\"Meloxicam\": Nonsteroidal anti-Inflammatory drug. It can treat osteoarthritis and rheumatoid arthritis.",
                             "\"मेलॉक्सिकैम\": नॉनस्टेरॉयड एंटी-इन्फ्लैमरेटरी दवा। यह ऑस्टियोआर्थराइटिस और रूमेटोइड गठिया का इलाज कर सकता है।",
                             "\"美洛昔康\"：非甾体类抗炎药。 它可以治疗骨关节炎和类风湿性关节炎。",
                             "\"Meloxicam\": medicamento antiinflamatorio no esteroideo. Puede tratar la osteoartritis y la artritis reumatoide.",
                             ]

databaseDict['Cyclobenzaprine'] = ["\"Cyclobenzaprine\": Muscle relaxant It can treat pain and stiffness caused by muscle spasms.",
                                   "\"साइक्लोबेनज़ाप्राइन\": मांसपेशियों में आराम करने वाला यह मांसपेशियों के स्पैम के कारण दर्द और कठोरता का इलाज कर सकता है।",
                                   "“环苯扎林”：肌肉松弛剂它可以治疗由肌肉痉挛引起的疼痛和僵硬。",
                                   "\"Ciclobenzaprina\": relajante muscular Puede tratar el dolor y la rigidez causados por los espasmos musculares.",
                                   ]

databaseDict['Lidocaine'] = ["\"Lidocaine\": Anesthetic and antiarrhythmic. It can treat irregular heartbeats (arrhythmias). It can also relieve pain and numb the skin.",
                            "\"लिडोकेन\": एनेस्थेटिक और एंटीरियथमिक। यह अनियमित दिल की धड़कन (एरिथमिया) का इलाज कर सकता है। यह दर्द से छुटकारा पा सकता है और त्वचा को खराब कर सकता है।",
                            "“利多卡因”：麻醉和抗心律失常。 它可以治疗不规则的心跳（心律失常）。 它还可以缓解疼痛和麻木。",
                            "\"Lidocaína\": anestésico y antiarrítmico. Puede tratar latidos cardíacos irregulares (arritmias). También puede aliviar el dolor y entumecer la piel"
                            ]

# x = databaseDict[salt][langEnumerator[lang]]

with open(fileOut, 'wb') as f:
    pickle.dump(databaseDict, f)