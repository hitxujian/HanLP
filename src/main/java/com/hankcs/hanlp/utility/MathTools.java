/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2014/05/23 17:09</create-date>
 *
 * <copyright file="MathTools.java" company="上海林原信息科技有限公司">
 * Copyright (c) 2003-2014, 上海林原信息科技有限公司. All Right Reserved, http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact 上海林原信息科技有限公司 to get more information.
 * </copyright>
 */
package com.hankcs.hanlp.utility;

import com.hankcs.hanlp.dictionary.CoreBiGramTableDictionary;
import com.hankcs.hanlp.seg.common.Vertex;

import static com.hankcs.hanlp.utility.Predefine.*;

/**
 * @author hankcs
 */
public class MathTools
{
    /**
     * 注意费用的定义：因为每个概率都很小，如果句子较长，最后得到的概率接近0，解决方法是求概率的对数之和，将乘法变成加法。
     * 词典概率对数都是负数，取反就变正了，把这个正数叫做费用
     * 概率乘法越大变成 -log(p) 加法越小越好
     * 从一个词到另一个词的词的花费
     * 这里的转移概率计算有点像    interpolation 平滑   
     * hat(pn)=b*pn+(1-b)*hat(pn-1)
     *p（from|to)
     * @param from 前面的词
     * @param to   当前的词
     * @return 分数
     */
    public static double calculateWeight(Vertex from, Vertex to)
    {
        int frequency = from.getAttribute().totalFrequency;
        if (frequency == 0)
        {
            frequency = 1;  // 防止发生除零错误
        }
        int nTwoWordsFreq = CoreBiGramTableDictionary.getBiFrequency(from.wordID, to.wordID);
        
        /**
         * 基本公式就是   -log{a*P(Ci-1)+(1-a)P(Ci|Ci-1)}
         * 可以看到是一种 中和的方法 就是不管ngram是不是0都会考虑n-1gram
         * 但实际计算的时候是如下
         weight = -Math.log(smooth* (1.0 + oneWordFreq)/ (Utility.MAX_FREQUENCE + 0.0)+ (1.0 - smooth)* ((1.0 - tinyDouble) * wordPairFreq/ (1.0 + oneWordFreq) + tinyDouble)); 
        */
       // double value = -Math.log(   dSmoothingPara * frequency / (MAX_FREQUENCY) + 
       // 		                    (1 - dSmoothingPara) * ((1 - dTemp) * nTwoWordsFreq / frequency + dTemp)
       // 		                );
        double value = -Math.log(   dSmoothingPara * frequency / (MAX_FREQUENCY) + 
                (1 - dSmoothingPara) * ( nTwoWordsFreq / frequency)
            );

        if (value < 0.0)
        {
            value = -value;
        }
//        logger.info(String.format("%5s frequency:%6d, %s nTwoWordsFreq:%3d, weight:%.2f", from.word, frequency, from.word + "@" + to.word, nTwoWordsFreq, value));
        return value;
    }
}
