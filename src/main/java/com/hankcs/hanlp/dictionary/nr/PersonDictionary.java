/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2014/9/10 14:47</create-date>
 *
 * <copyright file="PersonDictionary.java" company="上海林原信息科技有限公司">
 * Copyright (c) 2003-2014, 上海林原信息科技有限公司. All Right Reserved, http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact 上海林原信息科技有限公司 to get more information.
 * </copyright>
 */
package com.hankcs.hanlp.dictionary.nr;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.collection.AhoCorasick.AhoCorasickDoubleArrayTrie;
import com.hankcs.hanlp.corpus.dictionary.item.EnumItem;
import com.hankcs.hanlp.corpus.tag.NR;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.dictionary.CoreDictionary;
import com.hankcs.hanlp.dictionary.TransformMatrixDictionary;
import com.hankcs.hanlp.seg.common.Vertex;
import com.hankcs.hanlp.seg.common.WordNet;
import com.hankcs.hanlp.utility.Predefine;

import java.util.*;

import static com.hankcs.hanlp.corpus.tag.NR.*;
import static com.hankcs.hanlp.utility.Predefine.logger;
import static com.hankcs.hanlp.dictionary.nr.NRConstant.*;

/**
多模式匹配问题参考
http://www.hankcs.com/program/algorithm/aho-corasick-double-array-trie.html
 */
public class PersonDictionary{
    /**
     * 人名词典
     */
    public static NRDictionary dictionary;
    /**
     * 转移矩阵词典
     */
    public static TransformMatrixDictionary<NR> transformMatrixDictionary;
    /**
     * AC算法用到的Trie树
     */
    public static AhoCorasickDoubleArrayTrie<NRPattern> trie;//就是一些规则，定义哪几个tag在一起时一个人名

    public static final CoreDictionary.Attribute ATTRIBUTE = new CoreDictionary.Attribute(Nature.nr, 100);

    static{
        long start = System.currentTimeMillis();
        dictionary = new NRDictionary();
        if (!dictionary.load(HanLP.Config.PersonDictionaryPath)){
            throw new IllegalArgumentException("人名词典加载失败：" + HanLP.Config.PersonDictionaryPath);
        }
        transformMatrixDictionary = new TransformMatrixDictionary<NR>(NR.class);
        transformMatrixDictionary.load(HanLP.Config.PersonDictionaryTrPath);
        
        //构建 规则的过程
        trie = new AhoCorasickDoubleArrayTrie<NRPattern>();
        TreeMap<String, NRPattern> map = new TreeMap<String, NRPattern>();
        for (NRPattern pattern : NRPattern.values()){
            map.put(pattern.toString(), pattern);
        }
        trie.build(map);
        
        logger.info(HanLP.Config.PersonDictionaryPath + "加载成功，耗时" + (System.currentTimeMillis() - start) + "ms");
    }

    /**
     * 模式匹配,主要就是// 拆分UV
     *
     * @param nrList         确定的标注序列
     * @param vertexList     原始的未加角色标注的序列
     * @param wordNetOptimum 待优化的图
     * @param wordNetAll     全词图
     */
    public static void parsePattern(List<NR> nrList, List<Vertex> vertexList, final WordNet wordNetOptimum, final WordNet wordNetAll){
        
        ListIterator<Vertex> listIterator = vertexList.listIterator();
        
        NR preNR = NR.A;
        boolean backUp = false;
        int index = 0;
        
        
        
        
        StringBuilder sbPattern = new StringBuilder(nrList.size());
        //// 拆分UV
        //[K, B, C, V, K, K]
        //[ /K ,龚/B ,学/C ,平等/V ,领导/K , /K] 
        for (NR nr : nrList){//这里其实是遍历每个标注后的token
            ++index;
            Vertex current = listIterator.next();
            switch (nr){
            //这里重点是U和V  双字人名的末字和下文成词；   姓氏和上文成词
                case U:
                    if (!backUp){
                        vertexList = new ArrayList<Vertex>(vertexList);
                        listIterator = vertexList.listIterator(index);
                        backUp = true;
                    }
                    sbPattern.append(NR.K.toString());
                    sbPattern.append(NR.B.toString());
                    preNR = B;
                    listIterator.previous();
                    String nowK = current.realWord.substring(0, current.realWord.length() - 1);
                    String nowB = current.realWord.substring(current.realWord.length() - 1);
                    listIterator.set(new Vertex(nowK));
                    listIterator.next();
                    listIterator.add(new Vertex(nowB));
                    continue;
                case V:
                    if (!backUp){
                        vertexList = new ArrayList<Vertex>(vertexList);
                        listIterator = vertexList.listIterator(index);
                        backUp = true;
                    }
                    if (preNR == B)
                    {
                        sbPattern.append(NR.E.toString());  //BE
                    }
                    else
                    {
                        sbPattern.append(NR.D.toString());  //CD
                    }
                    sbPattern.append(NR.L.toString());
                    // 对串也做一些修改
                    listIterator.previous();
                    String nowED = current.realWord.substring(current.realWord.length() - 1);
                    String nowL = current.realWord.substring(0, current.realWord.length() - 1);
                    listIterator.set(new Vertex(nowED));
                    listIterator.add(new Vertex(nowL));
                    listIterator.next();
                    continue;
                default:
                    sbPattern.append(nr.toString());//非UV直接添加进去就好了
                    break;
            }
            preNR = nr;
        }//end for
     
        
        
        
        
        
        String pattern = sbPattern.toString();
       // System.out.println("模式串：{}"+pattern);
        //System.out.println("对应串：{}"+vertexList);
       
        final Vertex[] wordArray = vertexList.toArray(new Vertex[0]);
        final int[] offsetArray = new int[wordArray.length];
        offsetArray[0] = 0;
        for (int i = 1; i < wordArray.length; ++i){
            offsetArray[i] = offsetArray[i - 1] + wordArray[i - 1].realWord.length();
        }
        
        
        
        
        
        
        //比如针对  龚/学/平/等/领导    抽取出的pattern是    [ BCDLK]
        //注意这里的 pattern就是需要进行多模式匹配的文本
        trie.parseText(pattern, new AhoCorasickDoubleArrayTrie.IHit<NRPattern>(){
            @Override
            public void hit(int begin, int end, NRPattern value){
                StringBuilder sbName = new StringBuilder();
                for (int i = begin; i < end; ++i){
                    sbName.append(wordArray[i].realWord);
                }
                String name = sbName.toString();
                // 对一些bad case做出调整
                switch (value){
                    case BCD://  这里考虑双字的名字，C作为双字名字的第一个字；D作为双字名字的第二个字；B是姓，如果出现了这种模式的话
                        if (name.charAt(0) == name.charAt(2)) 
                        	return; // 姓和最后一个名不可能相等的
                        	break;
                }
                if (isBadCase(name)) 
                	return;

                // 正式算它是一个名字
                if (HanLP.Config.DEBUG){
                    System.out.printf("识别出人名：%s %s\n", name, value);
                }
                int offset = offsetArray[begin];
                wordNetOptimum.insert(offset, new Vertex(Predefine.TAG_PEOPLE, name, ATTRIBUTE, WORD_ID), wordNetAll);
            }
        });
        
        
        
        
    }

    /**
     * 因为任何算法都无法解决100%的问题，总是有一些bad case，这些bad case会以“盖公章 A 1”的形式加入词典中<BR>
     * 这个方法返回人名是否是bad case
     *
     * @param name
     * @return
     */
    static boolean isBadCase(String name){
    	//
        EnumItem<NR> nrEnumItem = dictionary.get(name);
        if (nrEnumItem == null) 
        	return false;
        return nrEnumItem.containsLabel(NR.A);
    }
}
