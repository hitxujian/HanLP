package aaa.xujian.formCorpus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aaa.chinese.prepocess.LangUtils;

/**
ctb格式转为bmes格式
 */
public class CTB2BMES {

	public static String regexp="([0-9]+)";
	public static Integer extractNumeric(String fileName){
		Integer numer=null;
		//String 中提取数字
		Pattern p=Pattern.compile(regexp);
		Matcher m=p.matcher(fileName);
		while(m.find()){
			numer=Integer.valueOf( m.group(0));
		}
		return numer;
	}
	
public static void readCtbAndConvert(String inputDir,String outfileTrain,String outfileTest) throws IOException{
		
		BufferedWriter bwTrain=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outfileTrain)), "utf-8"));
		BufferedWriter bwTest=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outfileTest)), "utf-8"));
		
		
		BufferedReader br=null;
		File inDir=new File(inputDir);
		
		if(inDir.isDirectory()){
			
			File[] files=inDir.listFiles();
			
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
					//一行代表一个句子,一个句子转为bmes格式
					int fileNo=extractNumeric(file.getName());
					if(fileNo>=4198&&fileNo<=4411){//测试
						convertSen2IOB(line,bwTest);
					}else{
						if(fileNo>=5000&&fileNo<=5558){//训练
							convertSen2IOB(line,bwTrain);
						}
					}
					
					//System.out.println(++cnt);
					
				}
			}
		}
		
		if(br!=null){
			br.close();
		}
		if(bwTrain!=null){
			bwTrain.close();
		}
		if(bwTest!=null){
			bwTest.close();
		}
	
	}

/**
 转换一个句子为bmes格式
上海 浦东 开发 与 法制 建设 同步
 */
	public static void convertSen2IOB(String line, BufferedWriter bw) throws IOException {
	

		line = LangUtils.preprocessText(line);
		if (line != null && line.length() > 0) {
			String[] word_list = line.trim().split(" ");
			for (String word : word_list) {
				
				
				if (word.length() == 1) {
					bw.write(word + "\t" + "S");
					bw.newLine();

				} else {
					char[] charsInWord = word.toCharArray();
					//System.out.println(line);
					try{
					bw.write(charsInWord[0] + "\t" + "B");
					}catch(ArrayIndexOutOfBoundsException e){
						//_NN
						System.out.println(line);
						continue;
					}
					bw.newLine();
					for (int i = 1; i < charsInWord.length - 1; i++) {
						bw.write(charsInWord[i] + "\t" + "M");
						bw.newLine();
					}
					bw.write(charsInWord[charsInWord.length-1] + "\t" + "E");
					bw.newLine();
				}

			}
			//// 一个句子处理完
			bw.newLine();
		}

	}

	
	
	public static void main(String[] args) {
		String inputDir="D:\\data\\ctb8\\segmented";
		String outputFileTrain="D:\\data\\data\\trainCorpus\\ctb_seg_bmes_train.txt";
		String outputFileTest="D:\\data\\data\\trainCorpus\\ctb_seg_bmes_text.txt";
		try {
			readCtbAndConvert(inputDir,outputFileTrain,outputFileTest);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
