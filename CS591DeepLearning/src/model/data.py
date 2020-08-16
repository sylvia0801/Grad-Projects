import numpy as np
import tensorflow.keras.preprocessing as preprocessing
from tensorflow.keras.applications.xception import preprocess_input
from tensorflow.keras.preprocessing.image import DirectoryIterator, load_img, img_to_array

from PIL.Image import Image
from typing import Union, Type

DATA_PATH = 'data/'
loader = preprocessing.image.ImageDataGenerator(preprocessing_function=preprocess_input)

def load_train_data() -> DirectoryIterator:
    '''
    Load train data and infer classes from the directory structure
    '''

    return loader.flow_from_directory(directory=f'{DATA_PATH}train/', target_size=(150,150))

def load_test_data() -> DirectoryIterator:
    '''
    Load test data and infer classes from the directory structure
    '''

    return loader.flow_from_directory(directory=f'{DATA_PATH}test/', target_size=(150,150))

def load_predict_data(image: Union[str, Type[Image]]) -> np.ndarray:
    '''
    Load an image for prediction

    :param images: a path to image relative to src/ or a PIL.Image.Image object
    :returns: np.ndarray representing the image
    '''

    if type(image) == str:
        return preprocess_input(img_to_array(load_img(image, target_size=(150,150))))
    elif issubclass(image.__class__, Image):
        return preprocess_input(img_to_array(image))
    else:
        raise ValueError(f'image argument of type {type(image)} is unacceptable')