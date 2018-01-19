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
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;

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
        String[] testCase = new String[]{
                "水的沸点是一百度",
                "这些问题请下课以后一一百度解决"
                };
       // for (String sentence : testCase)
        //{
            List<Term> termList = HanLP.segment("水的沸点是一百度");
            System.out.println(termList);
       // }
    }
}
