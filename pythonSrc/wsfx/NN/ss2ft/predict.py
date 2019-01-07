# coding: utf-8

from __future__ import print_function

import os
import tensorflow as tf
import tensorflow.contrib.keras as kr
import sys

sys.path.append("..")
from ss2ft.cnn_model2 import TCNNConfig, TextCNN
from preprocess import preprocess
from fileop import getlines

try:
    bool(type(unicode))
except NameError:
    unicode = str

#(以下路径必须使用绝对路径，相对路径的话，命令行运行不会报错，但是Java调用各种报错)
corpuspath = 'D:/Documents/GitHub/ECNet-J2EE/pythonSrc/wsfx/NN/ss2ft/model_store/source.txt'
vocab_dir = 'D:/Documents/GitHub/ECNet-J2EE/pythonSrc/wsfx/NN/ss2ft/model_store/vocab.txt'

save_path = 'D:/Documents/GitHub/ECNet-J2EE/pythonSrc/wsfx/NN/ss2ft/model_store/checkpoints/textcnn/best_validation'  # 最佳验证结果保存路径
tensorboard_dir = 'D:/Documents/GitHub/ECNet-J2EE/pythonSrc/wsfx/NN/ss2ft/model_store/tensorboard/textcnn'
stoplist1 = getlines('D:/Documents/GitHub/ECNet-J2EE/pythonSrc/data/stopwords.txt')
stoplist2 = getlines('D:/Documents/GitHub/ECNet-J2EE/pythonSrc/data/num_20words.txt')

p = preprocess(corpuspath, vocab_dir)
vocab = p.getvocab()

class CnnModel:
    def __init__(self):
        self.config = TCNNConfig()
        self.model = TextCNN(self.config)

        self.session = tf.Session()
        self.session.run(tf.global_variables_initializer())
        saver = tf.train.Saver()
        saver.restore(sess=self.session, save_path=save_path)  # 读取保存的模型

    def predict(self, input1, input2):
        # 支持不论在python2还是python3下训练的模型都可以在2或者3的环境下运行
        ssls = p.precessinput(input1,stoplist1,stoplist2)#预处理后的事实
        ftls = p.precessinput(input2,stoplist1,stoplist2)
        data_1 = [p.getwordid(vocab, word, 0) for word in ssls]
        data_2 = [p.getwordid(vocab, word, 0) for word in ftls]

        feed_dict = {
            self.model.input_x_1: kr.preprocessing.sequence.pad_sequences([data_1], self.config.seq_length_1),
            self.model.input_x_2: kr.preprocessing.sequence.pad_sequences([data_2], self.config.seq_length_2),
            self.model.keep_prob: 1.0
        }

        y_pred_cls = self.session.run(self.model.y_pred_cls, feed_dict=feed_dict)
        return y_pred_cls[0]


