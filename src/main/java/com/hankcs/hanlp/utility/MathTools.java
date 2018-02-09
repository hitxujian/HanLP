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
     *
     * @param from 前面的词
     * @param to   后面的词
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
       //原作者
        double value = -Math.log(   dSmoothingPara * frequency / (MAX_FREQUENCY) + 
        		                    (1 - dSmoothingPara) * ((1 - dTemp) * nTwoWordsFreq / frequency + dTemp)
        		                );
 

        if (value < 0.0)
        {
            value = -value;
        }
//        logger.info(String.format("%5s frequency:%6d, %s nTwoWordsFreq:%3d, weight:%.2f", from.word, frequency, from.word + "@" + to.word, nTwoWordsFreq, value));
        return value;
    }
}
