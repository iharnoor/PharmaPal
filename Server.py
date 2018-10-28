import os

from flask import Flask, request, jsonify

from ImageToText import imageToText
import base64

app = Flask(__name__)


# root
@app.route("/")
def index():
    """
    this is a root dir of my server
    :return: str
    """
    return "This is root!!!!"


# GET
@app.route('/users/<user>')
def hello_user(user):
    """
    this serves as a demo purpose
    :param user:
    :return: str
    """
    return "Hello %s!" % user


# POST
@app.route('/api/postData/<lang>', methods=['POST'])
def get_text_prediction(lang):
    """
    predicts requested text whether it is ham or spam
    :return: json
    """
    json = request.get_json()
    print(json)
    if len(json['text']) == 0:
        return 'error invalid input'

    image_binary = base64.b64decode(json['text'])
    with open('image.jpg', 'wb') as f:
        f.write(image_binary)

    dict = imageToText('image.jpg', lang)
    return dict
    # return json['text']


@app.route('/image', methods=['GET', 'POST'])
def GetNoteText():
    if request.method == 'POST':
        file = request.files['pic']
        file.save('excedrineTest4.JPG')

        dict = imageToText('excedrineTest4.JPG')
        return dict
        # processImage(filename)
    else:
        return "Y U NO USE POST?"


if __name__ == '__main__':
    # app.run(host='0.0.0.0', port=5000)
    app.run(host='127.0.0.1', port=80)
