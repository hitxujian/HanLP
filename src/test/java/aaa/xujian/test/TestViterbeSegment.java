package aaa.xujian.test;

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
		//CustomDictionary.insert("叫床");
//		String text = "要叫床你好，小贝你好。";
//
//		List<Term> termList = segment.seg(text);
//		System.out.println(termList);
    	//HanLP.segment()
    	HanLP.Config.DEBUG=true;
    	Segment segment=new ViterbiSegment().enableCustomDictionary(false).enableNameRecognize(true).enablePlaceRecognize(false).enableOrganizationRecognize(true);
		String text="一双红色的耐克运动鞋。";
    	//String  text="龚学平等领导";
		//我/rr, 要/v, 听/v, 葫芦娃/nz, 的/ude1, 歌/n, 是/vshi, 葫芦娃/nz, 葫芦娃/nz, ，/w, 是/vshi, 赤裸/n, 化/v, 。/w]
		List<Term> segTermList=segment.seg(text) ;
		System.out.println(segTermList);
    	    
    	    
    }
}
