1、参考自http://blog.csdn.net/zhaojianting/article/details/78194317
double value = -Math.log(dSmoothingPara * frequency / (MAX_FREQUENCY) + (1 - dSmoothingPara) * ((1 - dTemp) * nTwoWordsFreq / frequency + dTemp))
double value = -Math.log(dSmoothingPara * frequency / (MAX_FREQUENCY) + (1 - dSmoothingPara) * ((1 - dTemp) * nTwoWordsFreq/dTemp));
    
frequency :核心词典中的词频
nTwoWordsFreq:共现词频
int MAX_FREQUENCY= 25146057
double dTemp =(double) 1 / MAX_FREQUENCY + 0.00001
dSmoothingPara =0.1
2、说明只有nlp分词会包含粗分结果之类的东西
List<Term> termList = NLPTokenizer.segment("中国科学院计算技术研究所的宗成庆教授正在教授自然语言处理课程");
System.out.println(termList);
说明
NLP分词NLPTokenizer会执行全部命名实体识别和词性标注。