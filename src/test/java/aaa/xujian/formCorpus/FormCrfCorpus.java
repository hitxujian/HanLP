package aaa.xujian.formCorpus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import com.hankcs.test.model.TestCRF;

/**
 注意这个类和  TestCRF是不一样的，因为那个类是做了数字替换的
 由于在HanLP实现的CRF分词解码算法中，数词被转换为M，英文被转换为W；所以在训练CRF分词之前，需要用相同的逻辑预处理语料。转换代码请参考：com.hankcs.test.model.TestCRF#compile
  但是一般情况不需要的
单词与词性之间使用“/”分割，如华尔街/nsf，且任何单词都必须有词性，包括标点等。
单词与单词之间使用空格分割，如美国/nsf 华尔街/nsf 股市/n。
支持用[]将多个单词合并为一个复合词，如[纽约/nsf 时报/n]/nz，复合词也必须遵守1和2两点规范。
 */
public class FormCrfCorpus {

	//ctb转为人民日报格式
	public static void ctbToRMRB(String inputDir,String outputFileStr) throws IOException{
		
		BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outputFileStr)), "utf-8"));
		BufferedReader br=null;
		File inDir=new File(inputDir);
		
		if(inDir.isDirectory()){
			
			File[] files=inDir.listFiles();
			long cnt=0;
			for(File file:files){
				br=new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
				String line=null;
				while((line=br.readLine())!=null){
					line=line.trim();
					if(line.length()==0)
						continue;
					if(line.startsWith("<")&&line.endsWith(">")){
						continue;
					}
					line=line.replaceAll("_", "/");
					System.out.println(++cnt);
					bw.append(line);
					bw.newLine();
				}
			}
		}
		
		if(br!=null){
			br.close();
		}
		if(bw!=null){
			bw.close();
		}
	
	}
	
	//testPrepareCRFTrainingCorpus
	
	
	
	public static void main(String[] args) throws Exception {
		String inputDir="D:\\data\\ctb8\\postagged";
		String outputFile="D:\\data\\data\\trainCorpus\\beforeCrf.txt";
		ctbToRMRB(inputDir,outputFile);
		//准备crf语料
		TestCRF testCrf=new TestCRF();
		testCrf.testPrepareCRFTrainingCorpus("D:\\data\\data\\trainCorpus","D:\\data\\data\\trainCorpus\\forCrf");
		
	}
}
