package aaa.xujian.formCorpus;

import java.util.LinkedList;
import java.util.List;

import com.hankcs.hanlp.corpus.dictionary.EasyDictionary;
import com.hankcs.hanlp.corpus.dictionary.NRDictionaryMaker;
import com.hankcs.hanlp.corpus.document.CorpusLoader;
import com.hankcs.hanlp.corpus.document.Document;
import com.hankcs.hanlp.corpus.document.sentence.word.IWord;
import com.hankcs.hanlp.corpus.document.sentence.word.Word;

public class TestNRDcitionaryMaker{

    public static void main(String[] args){
    	
    	/**输入1：注意这里的EasyDictionay词典是 普通词典文件，比如人名词典  格式  	 
    	就是     单词  标签1 标签1频率  标签2 标签2频率
    	   输入2：人名日报2014语料库
    	   输出 就是针对人名的 标注语料，比如 U  KB：
    	   U就是上文+姓 构成词
    	   K  
    	   B
    	   具体解析过程参考     NRDictionaryMaker.roleTag  
    	   如果当前词word  的标签是姓，而前一个词  pre+word 拼接后可以去词典找到
             那么需要将前面词的 词本身改为merge后的词；标签改为U
             并将当前词删掉
            
    	*/
    	
    	
    	//词典是hanlp自带的
        EasyDictionary dictionary = EasyDictionary.create("D:\\data\\data\\dictionary\\custom\\人名词典.txt");
        final NRDictionaryMaker nrDictionaryMaker = new NRDictionaryMaker(dictionary);
       //语料是人民日报2014的
        CorpusLoader.walk("C:\\Users\\xujian_gary\\Downloads\\2014", new CorpusLoader.Handler(){
            @Override
            public void handle(Document document)
            {
                List<List<Word>> simpleSentenceList = document.getSimpleSentenceList();
                List<List<IWord>> compatibleList = new LinkedList<List<IWord>>();
                for (List<Word> wordList : simpleSentenceList)
                {
                    compatibleList.add(new LinkedList<IWord>(wordList));
                }
                nrDictionaryMaker.compute(compatibleList);
            }
        });
        nrDictionaryMaker.saveTxtTo("C:\\Users\\xujian_gary\\Downloads\\结果\\person");
    }

}
