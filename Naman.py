from gtts import gTTS

text = "Naman"
print(text)
tts = gTTS('namaste, kya haal hai', lang='hi')
tts.save('hello.mp3')

