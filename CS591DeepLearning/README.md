# DpicNet

Deep Neural Network tailored-made for [Intel Multi-class Image Classification](https://www.kaggle.com/puneet6060/intel-image-classification).

## Dataset

The dataset of DpicNet comes from [Intel Multi-class Image Classification](https://www.kaggle.com/puneet6060/intel-image-classification), which is located in the following structure

```
src/
    data/
        predict/
        test/
            buildings/
            forest/
            glacier/
            mountain/
            sea/
            street/
        train/
            buildings/
            forest/
            glacier/
            mountain/
            sea/
            street/
```

Training and testing data are loaded by functions located in [data.py](src/model/data.py) with associated classes being inferred from the directory structure.

See [report.pdf](report/report.pdf) for more details

## Launch via Docker 

The best setting experimented with approximately 86% accuracy on testing dataset is deployed using Tensorflow Serving and Flask in Docker. Before following this instruction, make sure Docker is installed on your machine. To launch the model, follow the following command in the Terminal

```bash
$ cd src/serving
$ docker-compose up
```

Now, the model is serving on local machine port 80. To predict, simply make a POST request to `/predict` endpoint on port 80 with `image` in the body as follows

```bash
$ curl -F "image=@img.jpg" http://localhost/predict
# Sample output: {"predictions":["mountain"]}
```