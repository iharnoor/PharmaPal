import os

from flask import Flask, request, jsonify

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
@app.route('/api/post_some_data', methods=['POST'])
def get_text_prediction():
    """
    predicts requested text whether it is ham or spam
    :return: json
    """
    json = request.get_json()
    print(json)
    if len(json['text']) == 0:
        return jsonify({'error': 'invalid input'})

    return jsonify({'you sent this': json['text']})


@app.route('/getNoteText', methods=['GET', 'POST'])
def GetNoteText():
    if request.method == 'POST':
        file = request.files['pic']
        filename = file.filename
        print(os.path)
        file.save(filename)
        # run Carl's code and store the string returned in a variable
        # return that string
        return "success"
        # processImage(filename)
    else:
        return "Y U NO USE POST?"


if __name__ == '__main__':
    # app.run(host='0.0.0.0', port=5000)
    app.run(host='127.0.0.1', port=80)
