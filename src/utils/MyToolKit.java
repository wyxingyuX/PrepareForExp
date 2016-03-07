package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;


public class MyToolKit {
	public static int getTypeNum(String filePath,String seperater) throws IOException{
		BufferedReader reader=FileTool.getBufferedReaderFromFile(filePath);
		int typeNum=0;
		String line="";
		while((line=reader.readLine())!=null){
			String[] elms=line.split(seperater);
			if(elms.length==2){
				++typeNum;
			}
		}
		System.out.println("this file have "+typeNum+" type");
		return typeNum;
	}
	public static void testResult(String filePath,String seperater) throws IOException{
		BufferedReader reader=FileTool.getBufferedReaderFromFile(filePath);
		int maxLikehoodType=0;
		double maxValue=0;
		double total=0;
		String line="";
		int lineNum=1;
		int end=40;
		while((line=reader.readLine())!=null){
			if(lineNum>end) break;
			String[] elms=line.split(seperater);
			String[] vElms=new String[elms.length-1];
			for(int i=1;i<elms.length;++i){
				double d=Double.parseDouble(elms[i]);
				if(d>maxValue){
					maxValue=d;
					maxLikehoodType=i;
				}
				total+=d;
				vElms[i-1]=elms[i];
			}
			System.out.println(lineNum+" row predict type is " +elms[0]+", the "+maxLikehoodType+"th Value is maxValue "
					+maxValue+", total have "+(elms.length-1)+" probality value,"+",Sum all value is "+total);
			System.out.println(getMaxValueFromStrArray(vElms));
			maxLikehoodType=0;
			maxValue=0;
			total=0;
			++lineNum;
		}

	}
	public static double getMaxValueFromStrArray(String[] dStr){
		double maxValue=-1000000000;
		for(int i=0;i<dStr.length;++i){
			double dValue=Double.parseDouble(dStr[i]);
			if(maxValue<dValue){
				maxValue=dValue;
			}
		}
		return maxValue;
	}
	public static List<String> cloneStrList(List<String> list){
		List<String> clone=new LinkedList<String>();
		for(String s:list){
			clone.add(s);
		}
		return clone;
	}

	public static int max(int a,int b,int c){
		return Math.max(Math.max(a, b), c);
	}
	public static int min(int a,int b,int c){
		return Math.min(Math.min(a, b), c);
	}
	public static boolean isInRange(double value,double startR,double endR){
		return (Double.compare(value, startR)>0)&&(Double.compare(value,endR)<0);
	}
	public static boolean isInorOnRange(double value,double startR,double endR){
		return (Double.compare(value, startR)>=0)&&(Double.compare(value,endR)<=0);
	}
	public static boolean equals(double value,double base,double maxError){//maxError 
		return isInRange(value, base-maxError, base+maxError);
	}
	public static int getMaxItemNumInPosElm(String source,String sourceSeperater,
			int pos,String itemSeperater) throws IOException{
		BufferedReader reader=FileTool.getBufferedReaderFromFile(source);
		String line="";
		int maxNum=0;
		while((line=reader.readLine())!=null){
			String[] elms=line.split(sourceSeperater);
			if(pos>=elms.length){
				System.out.println(elms[0]+" no "+pos+"th elm");
				continue;
			}
			String hitElm=elms[pos];
			String[] items=hitElm.split(itemSeperater);
			if(maxNum<items.length){
				maxNum=items.length;
			}
		}
		return maxNum;
	}

	public static int getMinItemNumInPosElm(String source,String sourceSeperater,
			int pos,String itemSeperater) throws IOException{
		BufferedReader reader=FileTool.getBufferedReaderFromFile(source);
		String line="";
		int minNum=Integer.MAX_VALUE;
		while((line=reader.readLine())!=null){
			String[] elms=line.split(sourceSeperater);
			if(pos>=elms.length){
				System.out.println(elms[0]+" no "+pos+"th elm");
				continue;
			}
			String hitElm=elms[pos];
			String[] items=hitElm.split(itemSeperater);
			if(minNum>items.length){
				minNum=items.length;
			}
		}
		return minNum;
	}

	public static int getAvgItemNumInPosElm(String source,String sourceSeperater,
			int pos,String itemSeperater) throws IOException{
		BufferedReader reader=FileTool.getBufferedReaderFromFile(source);
		String line="";
		int avgNum=0;
		int count=0;
		while((line=reader.readLine())!=null){
			String[] elms=line.split(sourceSeperater);
			if(pos>=elms.length){
				System.out.println(elms[0]+" no "+pos+"th elm");
				continue;
			}

			++count;
			String hitElm=elms[pos];
			String[] items=hitElm.split(itemSeperater);
			avgNum+=items.length;
		}
		avgNum/=count;
		return avgNum;
	}


	//splitPos是从第几个 seperater 劈开文件
	public static void splitFileElms2Files(String sourceFile,String sourceSeperater,int splitPos,String destFile1,String destFile2,String destSeperater) throws Exception{
		BufferedReader brSource=new BufferedReader(new FileReader(sourceFile));
		FileTool.guaranteeFileDirExist(destFile1);
		FileTool.guaranteeFileDirExist(destFile2);
		FileWriter fw1=new FileWriter(destFile1);
		FileWriter fw2=new FileWriter(destFile2);

		System.out.println("Start splitFileElms2Files from "+sourceFile);
		String curLine="";
		String[] elms=null;
		int lineNum=0;
		int readNum=0;
		while((curLine=brSource.readLine())!=null){
			++lineNum;
			if(curLine.equals("")){continue;}
			elms=curLine.split(sourceSeperater);
			for(int i=0;i<splitPos;++i)
			{
				if(i==splitPos-1)
				{
					fw1.write(elms[i]);
				}
				else
				{
					fw1.write(elms[i]+destSeperater);
				}
			}
			fw1.write("\r\n");
			for(int i=splitPos;i<elms.length;++i)
			{
				if(i==elms.length-1)
				{
					fw2.write(elms[i]);
				}
				else
				{
					fw2.write(elms[i]+destSeperater);
				}

			}
			fw2.write("\r\n");
			++readNum;
		}
		System.out.println("Read "+lineNum+" line, "+readNum+" line have uid");
		//
		fw1.flush();
		fw2.flush();
		fw1.close();
		fw2.close();
		brSource.close();
		System.out.println("End splitFileElms2Files from "+sourceFile);
	}

	public static String strList2concateStr(List<String> strList,String seperater){
		StringBuilder stb=new StringBuilder();
		for(int i=0;i<strList.size();++i){
			if(i==strList.size()-1){
				stb.append(strList.get(i));
			}else{
				stb.append(strList.get(i)+seperater);
			}
		}
		return stb.toString();
	}

	public static void generateFoldTrainTestExpSet(String idCatesFile,String idsCateSeperater,
			String destDir,int fold) throws IOException{
		Map<String ,List<String>> cateIdsMap=new HashMap<String,List<String>>();
		BufferedReader reader=FileTool.getBufferedReaderFromFile(idCatesFile);
		String line="";
		while((line=reader.readLine())!=null){
			String[] elms=line.split(idsCateSeperater);
			String id=elms[0].trim();
			String cate=elms[1].trim();
			List<String> ids=cateIdsMap.get(cate);
			if(ids==null){
				ids=new ArrayList<String>();
				cateIdsMap.put(cate, ids);
			}
			ids.add(id);
		}
		for(Map.Entry<String, List<String>> entry:cateIdsMap.entrySet()){
			Collections.sort(entry.getValue());
		}

		for(int i=0;i<fold;++i){
			PrintWriter trainWriter=FileTool.getPrintWriterForFile(destDir+i+"\\train.txt");
			PrintWriter testWriter=FileTool.getPrintWriterForFile(destDir+i+"\\test.txt");
			for(Map.Entry<String, List<String>> entry:cateIdsMap.entrySet()){
				String cate=entry.getKey();
				List<String> ids=entry.getValue();
				int foldNum=ids.size()/fold;

				for(int k=0;k<ids.size();++k){
					if(k>=i*foldNum&&k<(i+1)*foldNum){
						testWriter.write(ids.get(k)+idsCateSeperater+cate+"\r\n");
					}else{
						trainWriter.write(ids.get(k)+idsCateSeperater+cate+"\r\n");
					}
				}
			}
			trainWriter.close();
			testWriter.close();
		}
	}

	public static void generaterFoldTrainValidTestExpSet(String idCatesFile,String idsCateSeperater,
			String destDir,int fold) throws IOException{
		Map<String ,List<String>> cateIdsMap=new HashMap<String,List<String>>();
		BufferedReader reader=FileTool.getBufferedReaderFromFile(idCatesFile);
		String line="";
		while((line=reader.readLine())!=null){
			String[] elms=line.split(idsCateSeperater);
			String id=elms[0].trim();
			String cate=elms[1].trim();
			List<String> ids=cateIdsMap.get(cate);
			if(ids==null){
				ids=new ArrayList<String>();
				cateIdsMap.put(cate, ids);
			}
			ids.add(id);
		}
		for(Map.Entry<String, List<String>> entry:cateIdsMap.entrySet()){
			//Collections.sort(entry.getValue());
			Collections.shuffle(entry.getValue());
		}

		for(int i=0;i<fold;++i){
			PrintWriter trainWriter=FileTool.getPrintWriterForFile(destDir+i+"\\train.txt");
			PrintWriter validationWriter=FileTool.getPrintWriterForFile(destDir+i+"\\validation.txt");
			PrintWriter testWriter=FileTool.getPrintWriterForFile(destDir+i+"\\test.txt");
			for(Map.Entry<String, List<String>> entry:cateIdsMap.entrySet()){
				String cate=entry.getKey();
				List<String> ids=entry.getValue();
				int foldNum=ids.size()/fold;

				for(int k=0;k<ids.size();++k){
					if(k>=i*foldNum&&k<(i+1)*foldNum){
						validationWriter.write(ids.get(k)+idsCateSeperater+cate+"\r\n");
					}else if(k>=((i+1)%fold)*foldNum&&k<((i+1)%fold+1)*foldNum){
						testWriter.write(ids.get(k)+idsCateSeperater+cate+"\r\n");
					}else{
						trainWriter.write(ids.get(k)+idsCateSeperater+cate+"\r\n");
					}
				}
			}
			trainWriter.close();
			validationWriter.close();
			testWriter.close();
		}
	}

	public static void mergeGenderFile(String femaleFile,String maleFile,String dest,String seperater) throws IOException{
		PrintWriter writer=FileTool.getPrintWriterForFile(dest);

		String [] files=new String[2];
		files[0]=femaleFile;
		files[1]=maleFile;

		for(int i=0;i<files.length;++i){
			String sourceFile=files[i];
			BufferedReader reader=FileTool.getBufferedReaderFromFile(sourceFile);
			String line="";
			while((line=reader.readLine())!=null){
				writer.write(line+seperater+(i+1)+"\r\n");
			}
		}
		writer.close();
	}

	public static String generateRandomVec(Random rnd,float min,float max,int maxDiem,String seperater){
		StringBuilder stb=new StringBuilder();
		for(int i=0;i<maxDiem;++i){
			float v=rnd.nextFloat()*(max-min)+min;
			if(i==maxDiem-1){
				stb.append(v);
			}else{
				stb.append(v+seperater);
			}
		}
		return stb.toString();
	}

	public static Map<Integer,List<String>>  getCateIdsMap(String smallIdSet,String smallIdSetSeperater
			,Map<String,Integer> allIdCateMap) throws Exception{

		Map<Integer,List<String>> cateIdsMap=new HashMap<Integer,List<String>>();
		BufferedReader reader=FileTool.getBufferedReaderFromFile(smallIdSet);
		String line="";
		while((line=reader.readLine())!=null){
			String[] elms=line.split(smallIdSetSeperater,2);
			String uid=elms[0].trim();
			Integer cate=allIdCateMap.get(uid);
			if(cate==null){
				throw new Exception("allIdCateMap don't have  uid:"+uid);
			}
			List<String> ids=cateIdsMap.get(cate);
			if(ids==null){
				ids=new ArrayList<String>();
			}
			ids.add(uid);
			cateIdsMap.put(cate, ids);
		}
		return cateIdsMap;
	}

	//cateSize: -1 mean this cateSize is the minCateSize, 
	//           0 mean this cateSzie is samll the minCateSize and is 10 times,like 10,20,30,...
	//           other mean this cateSzie is just client wanted size,but this size allways small the minCateSize,if this size large the minCateSize we cut size to minCateSize. 
	public static Set<String> generateCateBalanceIdSet(Map<Integer,List<String>> cateIdsMap,int cateSize){
		Set<String> idSet=new HashSet<String>();
		int minCateSize=Integer.MAX_VALUE;
		int everyCateSize=0;
		for(Map.Entry<Integer, List<String>> entry:cateIdsMap.entrySet()){
			System.out.println(entry.getKey()+" category:"+entry.getValue().size()+" user in cateIdsMap");
			if(minCateSize>entry.getValue().size()){
				minCateSize=entry.getValue().size();
			}
		}

		if(cateSize<0){
			everyCateSize=minCateSize;
		}
		if(cateSize==0){
			everyCateSize=(minCateSize/10)*10;
		}
		if(cateSize>minCateSize){
			everyCateSize=minCateSize;
		}
		if(cateSize>0&&cateSize<minCateSize){
			everyCateSize=cateSize;
		}
		System.out.println("every catogry size: "+everyCateSize+" ,minCateSize is:"+minCateSize);
		for(Map.Entry<Integer, List<String>> entry:cateIdsMap.entrySet()){
			List<String> items=entry.getValue();
			for(int i=0;i<everyCateSize;++i){
				idSet.add(items.get(i));
			}
		}	
		return idSet;
	}

	/*
	 * wordVecFile1 is normal word and vec,like "sport 0.001 0.2 0.1...."
	 * wordVecFile2 is specila word and vec, like"1000234_sport 0.23 0.23 0.1...."
	 */
	public static void wordVecConcate(
			String wordVecFile1,
			String wordVecFile2,
			String concateDestFile,
			String seperater) throws IOException
	{
		Map<String,String> wordVecMap=IO.readWordVecMap(wordVecFile1, seperater);
		BufferedReader reader=FileTool.getBufferedReaderFromFile(wordVecFile2);
		PrintWriter writer=FileTool.getPrintWriterForFile(concateDestFile);


		String line="";
		String itemSeperater="_";
		String unkStr="</s>";

		reader.readLine();
		int wordVecDiem=IO.readVecDiem(wordVecFile1, seperater);
		int specialWordVecDiem=IO.readVecDiem(wordVecFile2, seperater);
		writer.write("X"+seperater+(wordVecDiem+specialWordVecDiem)+"\r\n");

		while((line=reader.readLine())!=null)
		{
			String[] elms=line.split(seperater, 2);
			String specialWord=elms[0];
			String word="";
			if(!specialWord.equals(unkStr))
			{
				word=specialWord.split(itemSeperater, 2)[1];
			}
			else
			{
				word=unkStr;
			}
			String wordVec=wordVecMap.get(word);

			String specialVec=elms[1].trim();

			writer.write(specialWord+seperater+wordVec+seperater+specialVec+"\r\n");
		}
		writer.close();
	}
	public static int getVecDiem(String wordVecFile,String seperater) throws IOException
	{
		BufferedReader reader=FileTool.getBufferedReaderFromFile(wordVecFile);
		String line="";
		reader.readLine();
		line=reader.readLine();
		String[] elms=line.split(seperater);
		return elms.length-1;

	}

	public static void filterSameIdLineForVecFile(String srcIdValuleLine,String destIdValueLine,String seperater) throws IOException
	{
		Map<String,String> idValueMap=IO.readKeyValueMap(srcIdValuleLine, seperater);
		int diem=MyToolKit.getVecDiem(srcIdValuleLine, seperater);
		PrintWriter writer=FileTool.getPrintWriterForFile(destIdValueLine);
		writer.write(idValueMap.size()+seperater+diem+"\r\n");
		IO.writeMap(idValueMap, seperater, writer);
		writer.close();
	}
	public static void filterSameIdLine(String srcIdValuleLine,String destIdValueLine,String seperater) throws IOException
	{
		Map<String,String> idValueMap=IO.readKeyValueMap(srcIdValuleLine, seperater);

		PrintWriter writer=FileTool.getPrintWriterForFile(destIdValueLine);
		IO.writeMap(idValueMap, seperater, writer);
		writer.close();
	}
	
	public static List<Double> cptItemsAvgVec(String[] items,Map<String,List<Double>> keyVecMap)
	{
		List<String> itemList=new ArrayList<String>();
		for(String i:items)
		{
			itemList.add(i);
		}
		return cptItemsAvgVec(itemList,keyVecMap);
	}
	public static List<Double> cptItemsAvgVec(List<String> items,Map<String,List<Double>> keyVecMap)
	{
		List<Double> avgvec=new ArrayList<Double>();
		int count=0;
		for(String item:items)
		{
			List<Double> vec=keyVecMap.get(item);
			if(vec==null)
			{
				continue;
			}
			for(int i=0;i<vec.size();++i)
			{
				if(avgvec.size()<(i+1))
				{
					avgvec.add(vec.get(i));
				}
				else
				{
					
					avgvec.set(i, avgvec.get(i)+vec.get(i));
				}
			}
			count++;
		}
		
		for(int i=0;i<avgvec.size();++i)
		{
			avgvec.set(i, avgvec.get(i)/count);
		}
		
		return avgvec;
		
	}
	

	public static void main(String [] args) throws IOException{
		//getTypeNum("F:/ExpData/DataFromOther/qty/Data4Tritrain/IMDB_Freq_DataTextTf.txt","\\s{1,}");
		//testResult("F:/wy1223/svm/SVM_lg/result_lg.txt","\\s{1,}");
		//System.out.println(max(1,5,1));
		//		System.out.println(getMaxItemNumInPosElm("E:\\ExpData\\source\\nne\\publicInfo\\Tag-friVec\\idFridsReview.txt","\t\t"
		//				              ,3,"\t"));
//		Map<String,List<Double>> kvm=new HashMap<String,List<Double>>();
//		List<Double> v1=new ArrayList<Double>();v1.add(1.0);v1.add(2.0);
//		List<Double> v2=new ArrayList<Double>();v2.add(3.0);v2.add(4.0);
//		List<Double> v3=new ArrayList<Double>();v3.add(5.0);v3.add(5.0);
//		List<Double> v4=new ArrayList<Double>();v4.add(6.0);v4.add(7.0);
//		kvm.put("1", v1);
//		kvm.put("2", v2);
//		kvm.put("3", v3);
//		kvm.put("4", v4);
//		List<String> is=new ArrayList<String>();
//		is.add("1");is.add("2");
//		List<Double> av=cptItemsAvgVec(is,kvm);
//		System.out.println("hl");
	}
}
