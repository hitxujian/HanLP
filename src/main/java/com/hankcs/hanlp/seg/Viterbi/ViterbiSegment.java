/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2015/1/19 20:51</create-date>
 *
 * <copyright file="ViterbiSegment.java" company="上海林原信息科技有限公司">
 * Copyright (c) 2003-2014, 上海林原信息科技有限公司. All Right Reserved, http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact 上海林原信息科技有限公司 to get more information.
 * </copyright>
 */
package com.hankcs.hanlp.seg.Viterbi;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.recognition.nr.JapanesePersonRecognition;
import com.hankcs.hanlp.recognition.nr.PersonRecognition;
import com.hankcs.hanlp.recognition.nr.TranslatedPersonRecognition;
import com.hankcs.hanlp.recognition.ns.PlaceRecognition;
import com.hankcs.hanlp.recognition.nt.OrganizationRecognition;
import com.hankcs.hanlp.seg.WordBasedGenerativeModelSegment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.seg.common.Vertex;
import com.hankcs.hanlp.seg.common.WordNet;

import java.util.LinkedList;
import java.util.List;

/**
 * Viterbi分词器<br>
 * 也是最短路分词，最短路求解采用Viterbi算法
 *
 * @author hankcs
 */
public class ViterbiSegment extends WordBasedGenerativeModelSegment
{
	/**
	 * 分词过程中两次用到了  自定义词典，第一次是在    GenerateWordNet 影响词图
	 * 第二次是在combineByCustomDictionary，对最后的分词结果根据自定义词典合并
	 * 但是注意第二次的时候，长词的原则，也就是说你做了切分，但是自定义词典的词比较长可以覆盖切分的几个词那么就覆盖
	 * 如果自定义词典词语比较短，是不会替换的
	 *  CustomDictionary.insert("口交");
    	    System.out.println(segment.seg(
    	            "港口交通立交桥"
    	    ));
     是没有问题的
	 */
    @Override
    protected List<Term> segSentence(char[] sentence){

        WordNet wordNetAll = new WordNet(sentence);
        //1、用到了自定义词典
        GenerateWordNet(wordNetAll);//生成词图的过程
        //System.out.printf("粗分词网：\n%s\n", wordNetAll);

        if (HanLP.Config.DEBUG)
        {
            System.out.printf("粗分词网：\n%s\n", wordNetAll);
        }

        //-------这里得到了粗分结果
        List<Vertex> vertexList = viterbi(wordNetAll);

        if (config.useCustomDictionary)
        {
            if (config.indexMode > 0)
                combineByCustomDictionary(vertexList, wordNetAll);
            //2、最后结果根据自定义词典合并
            else combineByCustomDictionary(vertexList);
        }

        if (HanLP.Config.DEBUG)
        {
            System.out.println("粗分结果" + convert(vertexList, false));
        }

        // 数字识别
        //--------粗分结果以后可以做合并处理，这里具体合并过程可以参考  http://www.cnblogs.com/en-heng/p/6274881.html
        if (config.numberQuantifierRecognize)
        {
            mergeNumberQuantifier(vertexList, wordNetAll, config);
        }

        // 实体命名识别
        if (config.ner)
        {
            WordNet wordNetOptimum = new WordNet(sentence, vertexList);
            int preSize = wordNetOptimum.size();
            if (config.nameRecognize)
            {
                PersonRecognition.Recognition(vertexList, wordNetOptimum, wordNetAll);
            }
            if (config.translatedNameRecognize)
            {
                TranslatedPersonRecognition.Recognition(vertexList, wordNetOptimum, wordNetAll);
            }
            if (config.japaneseNameRecognize)
            {
                JapanesePersonRecognition.Recognition(vertexList, wordNetOptimum, wordNetAll);
            }
            if (config.placeRecognize)
            {
                PlaceRecognition.Recognition(vertexList, wordNetOptimum, wordNetAll);
            }
            if (config.organizationRecognize)
            {
                // 层叠隐马模型——生成输出作为下一级隐马输入
                vertexList = viterbi(wordNetOptimum);
                wordNetOptimum.clear();
                wordNetOptimum.addAll(vertexList);
                preSize = wordNetOptimum.size();
                OrganizationRecognition.Recognition(vertexList, wordNetOptimum, wordNetAll);
            }
            if (wordNetOptimum.size() != preSize)
            {
                vertexList = viterbi(wordNetOptimum);
                if (HanLP.Config.DEBUG)
                {
                    System.out.printf("细分词网：\n%s\n", wordNetOptimum);
                }
            }
        }

        // 如果是索引模式则全切分
        if (config.indexMode > 0)
        {
            return decorateResultForIndexMode(vertexList, wordNetAll);
        }

        // 是否标注词性
        if (config.speechTagging)
        {
            speechTagging(vertexList);
        }

        return convert(vertexList, config.offset);
    }

    private static List<Vertex> viterbi(WordNet wordNet)
    {
        // 避免生成对象，优化速度
        LinkedList<Vertex> nodes[] = wordNet.getVertexes();
        //这里的nodes是是一个数组，数组每个元素类型是   LinkedList<Vertex>,也就是说数组每个元素是一个List
        //这个nodes数组其实存储了词图每个行，因为每行可以有多个元素所以是一个list，这里也就可以看出每个Vertex代表了词图每个单元格
        //nodes[]每个元素代表了一个行，一个行可以有多个Vertext
        
        LinkedList<Vertex> vertexList = new LinkedList<Vertex>();
        
        //注意这里是单独处理第一个行，应为nodes[0]是起始节点不需要考虑
        for (Vertex node : nodes[1])
        {
        	//因为下表为0表示起始，所以这里单独设定
            node.updateFrom(nodes[0].getFirst());
        }
        //从第一个节点开始往后处理，因为第0个节点是开始，最后一个节点是结束
        for (int i = 1; i < nodes.length - 1; ++i)
        {
        	
            LinkedList<Vertex> nodeArray = nodes[i];//注意nodes【】每个元素是词图一个行，一个行可以有多个列所以是是一个list
            if (nodeArray == null) 
            	continue;
            //这里其实是遍历词图指定行的每个列
            for (Vertex node : nodeArray)
            {
                if (node.from == null) 
                	continue;
                for (Vertex to : nodes[i + node.realWord.length()])
                {
                    to.updateFrom(node);
                }
            }
        }
        Vertex from = nodes[nodes.length - 1].getFirst();
        while (from != null)
        {
            vertexList.addFirst(from);
            from = from.from;
        }
        return vertexList;
    }

  
}
