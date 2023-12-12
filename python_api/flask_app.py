import io

from flask import Flask, request, jsonify
import os
import json
import hashlib
import base64
import PIL.Image as Image

app = Flask(__name__)
MAX_IMAGE_SIZE = (224, 224)


def hash_image_buffer(image_buf):
    sha256 = hashlib.sha256()
    sha256.update(image_buf.read())
    return sha256.hexdigest()


def get_base64_image(path):
    with open(path, 'rb') as f:
        image = f.read()
    return base64.b64encode(image)


def get_quotes(quotes_path):
    with open(quotes_path, 'r') as f:
        quotes = json.load(f)
    return quotes


QUOTES = get_quotes(os.path.abspath('./quotes.json'))


@app.route('/quote/<int:quote_id>', methods=['GET'])
def get_quote(quote_id):
    if quote_id >= len(QUOTES) or quote_id < 0:
        return jsonify({'error': f'Quote not found with id {quote_id}!'}), 400
    quote = QUOTES[quote_id - 1]
    return jsonify(quote), 200


@app.route('/quote', methods=['GET'])
def get_all_quotes():
    return jsonify(QUOTES), 200


@app.route('/quote/add_pic/<int:quote_id>', methods=['POST'])
def add_pic(quote_id):
    """
    Sample usage: curl -X POST -F 'image=@/path/to/image.png' http://
    """
    if quote_id >= len(QUOTES) or quote_id < 0:
        return jsonify({'error': f'Quote not found with id {quote_id}!'}), 400
    # Read image from request
    image_buf = request.files['image']
    # Generate a unique filename for the image, using the image sha256 hash
    filename = hash_image_buffer(image_buf) + '.png'
    # Create a pil image from the image buffer
    image = Image.open(image_buf)
    # Resize image if it is too big
    if image.size[0] > MAX_IMAGE_SIZE[0] or image.size[1] > MAX_IMAGE_SIZE[1]:
        image = image.resize(MAX_IMAGE_SIZE)
    # Save image to disk under a directory named after the quote id
    path = os.path.abspath(f'./python_api/pics/{quote_id}/{filename}')
    os.makedirs(os.path.dirname(path), exist_ok=True)
    image.save(path)
    return jsonify({'success': f'Image {filename} saved!'}), 200


def hash_image_from_bytes(image_buf: bytes):
    sha256 = hashlib.sha256()
    sha256.update(image_buf)
    return sha256.hexdigest()


@app.route('/quote/add_pic_base64', methods=['POST'])
def add_pic_base64():
    args = request.args
    if 'quote_id' not in args:
        return jsonify({'error': 'quote_id parameter missing!'}), 400
    quote_id = int(args['quote_id'])
    if quote_id > len(QUOTES) or quote_id < 0:
        return jsonify({'error': f'Quote not found with id {quote_id}!'}), 400
    # Read image from request, it is the only thing in the body
    image_buf = request.data
    # Generate a unique filename for the image, using the image sha256 hash
    filename = hash_image_from_bytes(image_buf) + '.png'
    # Create a pil image from the image buffer
    image = base64.b64decode(image_buf)
    image = Image.open(io.BytesIO(image))
    # Resize image if it is too big
    if image.size[0] > MAX_IMAGE_SIZE[0] or image.size[1] > MAX_IMAGE_SIZE[1]:
        image = image.resize(MAX_IMAGE_SIZE)
    # Save image to disk under a directory named after the quote id
    path = os.path.abspath(f'./python_api/pics/{quote_id}/{filename}')
    os.makedirs(os.path.dirname(path), exist_ok=True)
    image.save(path)
    return jsonify({'success': f'Image {filename} saved!'}), 200

@app.route('/quote/get_pics/', methods=['GET'])
def get_pics():
    args = request.args
    if 'quote_id' not in args:
        return jsonify({'error': 'quote_id parameter missing!'}), 400
    quote_id = int(args['quote_id'])
    if quote_id > len(QUOTES) or quote_id < 0:
        return jsonify({'error': f'Quote not found with id {quote_id}!'}), 400
    # Create a json string of the base64 encoded images in the directory
    pics = []
    path = os.path.abspath(f'./python_api/pics/{quote_id}')
    if not os.path.exists(path):
        return jsonify(pics), 200
    image_filenames = os.listdir(path)
    for filename in image_filenames:
        image_path = os.path.join(path, filename)
        pics.append(str(get_base64_image(image_path))[2:-1])
    return jsonify(pics), 200


@app.route('/quote/get_one_pic/', methods=['GET'])
def get_one_pic():
    args = request.args
    if 'quote_id' not in args:
        return jsonify({'error': 'quote_id parameter missing!'}), 400
    quote_id = int(args['quote_id'])
    if quote_id > len(QUOTES) or quote_id < 1:
        return jsonify({'error': f'Quote not found with id {quote_id}!'}), 400
    # Create a json string of the base64 encoded images in the directory
    path = os.path.abspath(f'./python_api/pics/{quote_id}')
    if not os.path.exists(path):
        return jsonify({}), 200
    image_filenames = os.listdir(path)
    image_path = os.path.join(path, image_filenames[0])
    return jsonify(str(get_base64_image(image_path))), 200


if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)


