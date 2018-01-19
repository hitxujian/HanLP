1、参考自http://blog.csdn.net/zhaojianting/article/details/78194317
double value = -Math.log(dSmoothingPara * frequency / (MAX_FREQUENCY) + (1 - dSmoothingPara) * ((1 - dTemp) * nTwoWordsFreq / frequency + dTemp))
double value = -Math.log(dSmoothingPara * frequency / (MAX_FREQUENCY) + (1 - dSmoothingPara) * ((1 - dTemp) * nTwoWordsFreq/dTemp));
    
frequency :核心词典中的词频
nTwoWordsFreq:共现词频
int MAX_FREQUENCY= 25146057
double dTemp =(double) 1 / MAX_FREQUENCY + 0.00001
dSmoothingPara =0.1