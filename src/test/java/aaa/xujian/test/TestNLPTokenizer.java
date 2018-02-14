package aaa.xujian.test;

import java.util.List;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.Viterbi.ViterbiSegment;
import com.hankcs.hanlp.seg.common.Term;

public class TestNLPTokenizer {

	public static Segment nlpSegmentor = HanLP.newSegment().enableNameRecognize(true).enableTranslatedNameRecognize(true)
            .enableJapaneseNameRecognize(true).enablePlaceRecognize(true).enableOrganizationRecognize(true)
            .enablePartOfSpeechTagging(false);
	public static Segment viterbeSegmentor=new ViterbiSegment().enableCustomDictionary(true).enablePlaceRecognize(false).enableOrganizationRecognize(false);
	
	public static void main(String[] args) {
		//newSegment()实际就是维特比分词调用
		  
		  String text="陶某、陶某某路经该处时";
		  List<Term> nlp_result=nlpSegmentor.seg(text) ;
		  System.out.println(nlp_result);
		  List<Term> viterbe_result=viterbeSegmentor.seg(text) ;
		  System.out.println(viterbe_result);
		  
	}
}
