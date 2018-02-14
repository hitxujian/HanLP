1、参考自http://blog.csdn.net/zhaojianting/article/details/78194317
http://sparkgis.com/java/2017/10/hanlp%E7%AC%94%E8%AE%B002-%E7%BB%B4%E7%89%B9%E6%AF%94%E7%AE%97%E6%B3%95-%E5%8E%9F-hanlp%E7%AC%94%E8%AE%B002-%E7%BB%B4%E7%89%B9%E6%AF%94%E7%AE%97%E6%B3%95-swimmer/
double value = -Math.log(dSmoothingPara * frequency / (MAX_FREQUENCY) + (1 - dSmoothingPara) * ((1 - dTemp) * nTwoWordsFreq / frequency + dTemp))
double value = -Math.log(dSmoothingPara * frequency / (MAX_FREQUENCY) + (1 - dSmoothingPara) * ((1 - dTemp) * nTwoWordsFreq/dTemp));
这个已经修改过了
2、对于自定义词典，词频在分词的时候没啥用，词典主要两方面用
形成词图的时候候选词添加
最后如果可以合并，就是切分多个词但是可以合并成词典词那么就合并
3、对于训练数据参考
具体代码   com.hankcs.test.model.TestCRF.java  方法   testPrepareCRFTrainingCorpus()
https://github.com/hankcs/HanLP/wiki/%E8%AE%AD%E7%BB%83%E5%88%86%E8%AF%8D%E6%A8%A1%E5%9E%8B
具体语料格式
单词与词性之间使用“/”分割，如华尔街/nsf，且任何单词都必须有词性，包括标点等。
单词与单词之间使用空格分割，如美国/nsf 华尔街/nsf 股市/n。
支持用[]将多个单词合并为一个复合词，如[纽约/nsf 时报/n]/nz，复合词也必须遵守1和2两点规范。

4、对于经过二元模型以得到粗分结果，可以对结果做合并，就是所谓的数字人名等识别
http://www.cnblogs.com/en-heng/p/6274881.html
