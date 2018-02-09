/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2014/12/7 19:02</create-date>
 *
 * <copyright file="DemoSegment.java" company="上海林原信息科技有限公司">
 * Copyright (c) 2003-2014, 上海林原信息科技有限公司. All Right Reserved, http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact 上海林原信息科技有限公司 to get more information.
 * </copyright>
 */


import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.Dijkstra.DijkstraSegment;
import com.hankcs.hanlp.seg.Viterbi.ViterbiSegment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.BasicTokenizer;

import java.util.List;

/**
 * 标准分词
 *
 * @author hankcs
 */
public class TestViterbeSegment
{
    public static void main(String[] args)
    {
       
//		Segment segment = new ViterbiSegment().enableCustomDictionary(false);
//		CustomDictionary.insert("口交");
//		String text = "要叫床你好，小贝你好。";
//
//		List<Term> termList = segment.seg(text);
//		System.out.println(termList);
    	Segment segment=new ViterbiSegment().enableCustomDictionary(true).enablePlaceRecognize(false).enableOrganizationRecognize(false);
		String  text="唱一首歌叫床";
		List<Term> segTermList=segment.seg(text) ;
		System.out.println(segTermList);
    	    
    	    
    }
}
