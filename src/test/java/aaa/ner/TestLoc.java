package aaa.ner;

import java.util.Set;

import com.hankcs.hanlp.corpus.io.IOUtil;
import com.hankcs.hanlp.dictionary.common.CommonStringDictionary;
import com.hankcs.test.corpus.TestNTRecognition;

public class TestLoc {
	
	public void testGeneratePatternJavaCode() throws Exception{
        CommonStringDictionary commonStringDictionary = new CommonStringDictionary();
        commonStringDictionary.load("nt.pattern.txt");
        StringBuilder sb = new StringBuilder();
        Set<String> keySet = commonStringDictionary.keySet();
        //CommonStringDictionary secondDictionary = new CommonStringDictionary();
        //secondDictionary.load("data/dictionary/organization/outerNT.pattern.txt");
       // keySet.addAll(secondDictionary.keySet());
        for (String pattern : keySet)
        {
            sb.append("trie.addKeyword(\"" + pattern + "\");\n");
        }
       
        System.out.println(keySet);
        //IOUtil.saveTxt("data/dictionary/organization/code.txt", sb.toString());
    }
	
	
	 public static void main(String[] args) {
		 TestLoc test=new TestLoc();
	    	
	    	try {
				test.testGeneratePatternJavaCode();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

}
