# FROM tensorflow/serving
# WORKDIR /dpicnet
# COPY ./saved_models/s3/ /models/dpicnet/1/
# EXPOSE 8501

FROM tensorflow/tensorflow
WORKDIR /dpicnet
COPY ./app.py ./
RUN python3 -m pip install Pillow Flask
EXPOSE 80
CMD flask run -p 80 -h 0.0.0.0