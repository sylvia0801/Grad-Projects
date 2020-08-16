from os import environ
from flask import Flask, request
import requests
from PIL import Image
import numpy as np
from tensorflow.keras.applications.xception import preprocess_input
from tensorflow.keras.preprocessing.image import load_img, img_to_array

from typing import Union, Type

app = Flask('DpicNet')
classes = {0: 'buildings', 1: 'forest', 2: 'glacier', 3: 'mountain', 4: 'sea', 5: 'street'}
to_class_name = np.vectorize(lambda x: classes[x])

@app.route('/predict', methods=['POST'])
def predict():
    return {'predictions': to_class_name(np.array(requests.post(f'http://{environ["TFSERVE_SERVER"]}:{environ["TFSERVE_PORT"]}/v1/models/dpicnet:predict', json={
        'instances': [_load_predict_data(Image.open(request.files['image'])).tolist()]
        }).json()['predictions']).argmax(axis=1)).tolist()}

def _load_predict_data(image: Type[Image.Image]) -> np.ndarray:
    '''
    Load an image for prediction

    :param images: a path to image relative to src/ or a PIL.Image.Image object
    :returns: np.ndarray representing the image
    '''

    if issubclass(image.__class__, Image.Image):
        return preprocess_input(img_to_array(image))
    else:
        raise ValueError(f'image argument of type {type(image)} is unacceptable')