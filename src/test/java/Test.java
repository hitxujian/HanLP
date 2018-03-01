import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import com.hankcs.hanlp.corpus.document.sentence.word.IWord;
import com.hankcs.hanlp.corpus.io.IOUtil;

public class Test {

	public static void main(String[] args) throws IOException, IOException {
		 
		List<String> wordList=Arrays.asList("a","b","c","d","e");
		 ListIterator<String> listIterator = wordList.listIterator();
         while (listIterator.hasNext()){
        	 String word = listIterator.next();
        	 if("b".equals(word)){
        		 listIterator.remove();
        	 }
         }
		
		
	}
}
