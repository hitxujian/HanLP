/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2014/9/9 14:46</create-date>
 *
 * <copyright file="NRDictionaryMaker.java" company="上海林原信息科技有限公司">
 * Copyright (c) 2003-2014, 上海林原信息科技有限公司. All Right Reserved, http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact 上海林原信息科技有限公司 to get more information.
 * </copyright>
 */
package com.hankcs.hanlp.corpus.dictionary;

import com.hankcs.hanlp.corpus.document.CorpusLoader;
import com.hankcs.hanlp.corpus.document.Document;
import com.hankcs.hanlp.corpus.document.sentence.word.IWord;
import com.hankcs.hanlp.corpus.document.sentence.word.Word;
import com.hankcs.hanlp.corpus.tag.NR;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.corpus.util.Precompiler;
import com.hankcs.hanlp.utility.Predefine;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import static com.hankcs.hanlp.utility.Predefine.logger;

/**
 * nr词典（词典+ngram转移+词性转移矩阵）制作工具
 * @author hankcs
 */
public class NRDictionaryMaker extends CommonDictionaryMaker
{

    public NRDictionaryMaker(EasyDictionary dictionary)
    {
        super(dictionary);
    }

    @Override
    protected void addToDictionary(List<List<IWord>> sentenceList){
        logger.warning("开始制作词典");
        // 将非A的词语保存下来
        for (List<IWord> wordList : sentenceList) {
            for (IWord word : wordList) {
                if (!word.getLabel().equals(NR.A.toString())){
                	
                	//System.out.println(word);
                    dictionaryMaker.add(word);
                }
            }
        }
        
//        if(this.dictionaryMaker.trie.size()>0)
//           this.dictionaryMaker.saveTxtTo("C:\\Users\\xujian_gary\\Downloads\\结果\\temp.txt");
//        
        
       
        // 制作NGram词典
        for (List<IWord> wordList : sentenceList){
            IWord pre = null;
            for (IWord word : wordList){
                if (pre != null){
                    nGramDictionaryMaker.addPair(pre, word);
                }
                pre = word;
            }
        }
    }

    @Override
    protected void roleTag(List<List<IWord>> sentenceList){
        logger.info("开始标注角色");
        int i = 0;
        for (List<IWord> wordList : sentenceList){
        	
            logger.info(++i + " / " + sentenceList.size());
            
            if (verbose) 
            	System.out.println("原始语料----- " + wordList);
            
            
            
            
            
            
            
            
            
            
            //1、先标注A和K，主要是判断当前词和前一个词
            //如果当前词是人名，前面的词不是人名，那么前面的词改成人名上文
            IWord pre = new Word("##始##", "begin"); 
            ListIterator<IWord> listIterator = wordList.listIterator();
            while (listIterator.hasNext()){
            	//当前词
                IWord word = listIterator.next();
                if (!word.getLabel().equals(Nature.nr.toString())){
                	//当前词不是人名
                    word.setLabel(NR.A.toString());
                }else{
                	//当前词是人名，前面的词不是人名，那么前面的词设置成人名的上文
                    if (!pre.getLabel().equals(Nature.nr.toString())){
                        pre.setLabel(NR.K.toString());
                    }
                }
                pre = word;
            }
            if (verbose) 
            	System.out.println("标注非前------ " + wordList);
            
            
            
         
            
            
                 // 2、然后标注LM，
            IWord next = new Word("##末##", "end");
            while (listIterator.hasPrevious()){
                IWord word = listIterator.previous();
                if (word.getLabel().equals(Nature.nr.toString())){
                	//当前词是人名
                    String label = next.getLabel();
                    //这里equals的判断是  NR。java 而不是 Nature.java 
                    if (label.equals("A")){////后边的词啥也不是，那么就把后边的词设置成人名的下文
                    	next.setLabel(NR.L.toString());
                    }else if (label.equals(NR.K.toString())){//后边的词是人名的上文	又【来到】于洪洋的家。，那么久讲后面的词设置为  两个人名之间的成分
                    	next.setLabel(NR.M.toString());
                    }
                }
                next = word;
            }
            if (verbose) 
            	System.out.println("标注中后 " + wordList);
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            // 3、拆成前缀+姓，比如 “老刘”在人民日报是一个人名，这里要拆成      前缀+姓
            //  比如 ”王总“ 人民日报是一个名字，这里要拆分成  姓+后缀  （ NR.G）
            //比如 张丽  就拆成    B+E  姓+单名
            listIterator = wordList.listIterator();
            while (listIterator.hasNext()){
                IWord word = listIterator.next();
                if (word.getLabel().equals(Nature.nr.toString())){
                    switch (word.getValue().length()){
                        case 2:
                            if (  word.getValue().startsWith("大")|| word.getValue().startsWith("老")|| word.getValue().startsWith("小")  ){
                                listIterator.add(new Word(word.getValue().substring(1, 2), NR.B.toString()));
                                word.setValue(word.getValue().substring(0, 1));
                                word.setLabel(NR.F.toString());
                            }else if ( word.getValue().endsWith("哥")|| word.getValue().endsWith("公")|| word.getValue().endsWith("姐")
                                       || word.getValue().endsWith("老")|| word.getValue().endsWith("某")|| word.getValue().endsWith("嫂")
                                       || word.getValue().endsWith("氏")|| word.getValue().endsWith("总")   ) {
                                listIterator.add(new Word(word.getValue().substring(1, 2), NR.G.toString()));
                                word.setValue(word.getValue().substring(0, 1));
                                word.setLabel(NR.B.toString());
                            }else{
                                listIterator.add(new Word(word.getValue().substring(1, 2), NR.E.toString()));
                                word.setValue(word.getValue().substring(0, 1));
                                word.setLabel(NR.B.toString());
                            }
                            break;
                        case 3:
                            listIterator.add(new Word(word.getValue().substring(1, 2), NR.C.toString()));
                            listIterator.add(new Word(word.getValue().substring(2, 3), NR.D.toString()));
                            word.setValue(word.getValue().substring(0, 1));
                            word.setLabel(NR.B.toString());
                            break;
                    }
                }
            }
            if (verbose) 
            	System.out.println("姓名拆分 " + wordList);
            
          
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            
            //4、人名的上文和 姓 成词，
            /**如果当前词word  的标签是姓，而前一个词  pre+word 拼接后可以去词典找到
             * 那么需要将前面词的 词本身改为merge后的词；标签改为U
             * 并将当前词删掉
             */
            // 上文成词
            listIterator = wordList.listIterator();
            pre = new Word("##始##", "begin");
            while (listIterator.hasNext()){
                IWord word = listIterator.next();
                if (word.getLabel().equals(NR.B.toString())){
                	//当前词是姓，前面词+当前词组合存在于词典
                    String combine = pre.getValue() + word.getValue();
                    if (dictionary.contains(combine)){
                        pre.setValue(combine);
                        pre.setLabel(NR.U.toString());
                        listIterator.remove();
                    }
                }
                pre = word;
            }
            if (verbose) 
            	System.out.println("上文成词 " + wordList);
            
            
      
            
            
            
            
            
            
            
            
            
            
            // 5、头部成词
            //  X Pfm	姓与双名的首字成词	【王国】维、
            //	Y Pfs	姓与单名成词	【高峰】、【汪洋】
            next = new Word("##末##", "end");
            while (listIterator.hasPrevious()){
                IWord word = listIterator.previous();
                if (word.getLabel().equals(NR.B.toString())){
                	//当前词是姓，当前词+后边词 在人名词典中
                    String combine = word.getValue() + next.getValue();
                    if (dictionary.contains(combine)){
                        next.setValue(combine);
                        //判断后边的词性是不是  	Pm	双名的首字	张【华】平先生
                        next.setLabel(next.getLabel().equals(NR.C.toString()) ? NR.X.toString() : NR.Y.toString());
                        listIterator.remove();
                    }
                }
                next = word;
            }
            if (verbose) 
            	System.out.println("头部成词 " + wordList);
            
            
            
            
            
            
            
            //6、 尾部成词
            // Z   Pmt	双名本身成词	张【朝阳】
           
            pre = new Word("##始##", "begin");
            while (listIterator.hasNext()){
                IWord word = listIterator.next();
                if (word.getLabel().equals(NR.D.toString())){
                	//当前词是  双名的末字	张华【平】先生，然后前面的词+当前词 如果在词典
                    String combine = pre.getValue() + word.getValue();
                    if (dictionary.contains(combine)){
                        pre.setValue(combine);
                        pre.setLabel(NR.Z.toString());
                        listIterator.remove();
                    }
                }
                pre = word;
            }
            if (verbose)
            	System.out.println("尾部成词 " + wordList);
          
            
            
            
            
            // 7、下文成词
            //	V  三字人名的末字和下文成词	龚学平等领导, 邓颖【超生】前
            next = new Word("##末##", "end");
            while (listIterator.hasPrevious()){
                IWord word = listIterator.previous();
                if (word.getLabel().equals(NR.D.toString())){
                    String combine = word.getValue() + next.getValue();
                    if (dictionary.contains(combine)){
                        next.setValue(combine);
                        next.setLabel(NR.V.toString());
                        listIterator.remove();
                    }
                }
                next = word;
            }
            if (verbose) 
            	System.out.println("头部成词 " + wordList);
            
            
            
            
            
            
            
            
            
            
            LinkedList<IWord> wordLinkedList = (LinkedList<IWord>) wordList;
            wordLinkedList.addFirst(new Word(Predefine.TAG_BIGIN, "S"));
            wordLinkedList.addLast(new Word(Predefine.TAG_END, "A"));
            if (verbose) 
            	System.out.println("添加首尾 " + wordList);
        }
    }
}
