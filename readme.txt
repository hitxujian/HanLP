》》》兼容人名日报标签（比如人名）的定义文件在
Nature.java---是个词性定义，比如形容词，人名等等 是个广义定义
hanlp针对人名识别的语料是在 NR.java 中
NR.java 是人名的细化，比如姓，双字名字的首字，姓上文等等
》》》》处理逻辑实在  NRDictionaryMaker.java 中
》》》》形成人名识别语料   TestNRDcitionaryMaker.java这个文件
修改读取文件个数可以在  CorpusLoader.java 中修改

》》》人名识别模式串是在文件  PersonRecognition,人名词典的加载是在   PersonDictionary
》》
  NRPattern.java 中的
具体测试可以如下
Segment segment=new ViterbiSegment().enableCustomDictionary(false).enableNameRecognize(true).enablePlaceRecognize(false).enableOrganizationRecognize(true);
String  text="老徐建是个不错的小伙子。";
http://www.hankcs.com/nlp/segment/ictclas-the-hmm-name-recognition.html		

》》》》人名势必别


自己可以测试下文中作者所说的方法
“
一般人的想法是机械的匹配，匹配到什么就是什么。但是这里面其实是有复杂的规则的，而且都是一些经验规则。比如：
Rule 1 for exclusion:前缀+姓+名1(名2): 规则(前缀+姓)失效；
Rule 2 for exclusion:姓+单名+单名:单名+单名 若EE对应的字不同，规则失效.如：韩磊磊
Rule 3 for exclusion: 若姓后不是后缀，规则失效.如：江主席、刘大娘
……
”
在hanlp中其实没有实现的。
还可以测试一个例子：
龚学平等领导
》》》地名识别过程
OrganizationRecognition.java 就是实现地址识别的过程，
具体测试过程就是   TestLoc.java 这个文件
对于经过二元模型以得到粗分结果，可以对结果做合并，就是所谓的数字人名等识别









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





参考文献：
http://www.cnblogs.com/en-heng/p/6274881.html  

