import numpy as np
# import tensorflow as tf
# import tensorflow.keras as keras
from tensorflow.keras.models import load_model
from tensorflow.keras.applications.xception import Xception
from tensorflow.keras.layers import Flatten, Dense
from tensorflow.keras import Sequential
from tensorflow.keras.preprocessing.image import DirectoryIterator, load_img
from tensorflow.keras.regularizers import l1_l2#, l1, l2

from typing import Tuple, Union
from PIL.JpegImagePlugin import JpegImageFile

class DpicNet():
    def __init__(self, input_shape: Union[Tuple[int, int, int], bool], classes: int, hidden_nodes: Tuple[int, ...], regularize: Tuple[Union[None, str], ...]):
        self._class_mapping = {0: 'buildings', 1: 'forest', 2: 'glacier', 3: 'mountain', 4: 'sea', 5: 'street'}

        if input_shape != False: # input_shape is used as a flag whether to load saved model
            # DpicNet model
            self._model = Sequential(name='DpicNet')

            # Transfer Xception model
            xception = Xception(include_top=False, input_shape=input_shape)

            ## Mark as nontrainable
            for layer in xception.layers:
                layer.trainable = False
            
            ## Add to DpicNet
            self._model.add(xception)

            ## Flatten before classifier layers    
            self._model.add(Flatten())

            ## Classifier layers
            for n, r in zip(hidden_nodes, regularize):
                self._model.add(Dense(n, activation='relu', kernel_regularizer=l1_l2() if r == 'l1_l2' else r))

            ## Output layer
            self._model.add(Dense(classes, activation='softmax'))

            # DpicNet
            self._model.compile(optimizer='adam', loss='categorical_crossentropy', metrics=['accuracy'])

    def __str__(self):
        self._model.summary()
        return ''

    def save(self, path: str) -> None:
        '''
        Save model in SavedModel format
        '''
        
        self._model.save(path)

    @classmethod
    def load(cls, path: str):
        '''
        Load saved model from SavedModel format
        '''

        net = cls(False, 0, 0, 0)
        net._model = load_model(path)
        net._model.build(net._model.get_layer('xception').input_shape)
        net._model.get_layer('xception').trainable = False

        return net
    
    def fit(self, data: DirectoryIterator, epoch: int) -> None:
        '''
        Train model on the given data
        '''

        self._model.fit(data, epochs=epoch)

    def evaluate(self, data: DirectoryIterator) -> np.float64:
        '''
        Evaluate model accuracy on the given data
        '''

        return self._model.evaluate(data)

    def predict(self, image: np.ndarray) -> Tuple[int, str]:
        '''
        Make prediction on the given image and return predicted class index and name

        :param image: np.ndarray object representing image to predict
        :returns: A tuple containing prediction class index and name
        '''

        assert image.ndim == 3 and image.shape[0] == image.shape[1] == 150 and image.shape[2] == 3, 'An image must be in shape (150,150,3)'

        pred_class = self._model.predict_classes(image.reshape(1,150,150,3))[0]
        return (pred_class, self._class_mapping[pred_class])