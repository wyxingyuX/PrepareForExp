package utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Bean.WinterHoliday.UserDataFull;
import func.Stat;

public class IO {
	public static List<String> readPosElmFromFile(String filePath,String seperater,int pos) throws Exception{
		List<String> list=new LinkedList<String>();
		BufferedReader reader=FileTool.getBufferedReaderFromFile(filePath);
		String line="";
		while((line=reader.readLine())!=null){
			String[] elms=line.split(seperater);
			if(pos<-1||pos>=elms.length){
				throw new Exception("pos<-1||pos>=elms.length");
			}
			list.add(elms[pos]);
		}
		return list;
	}
	public static Map<String,List<String>> readUidItems(String allUidItems,String seperater) throws IOException{
		Map<String,List<String>> uidItems=new HashMap<String,List<String>>(50*10000);
		BufferedReader reader=FileTool.getBufferedReaderFromFile(allUidItems);
		String line="";
		int lineNum=0;
		int mod=1000;
		while((line=reader.readLine())!=null){
			String [] elms=line.split(seperater);
			List<String> items=new LinkedList<String>();
			for(int i=1;i<elms.length;++i){
				items.add(elms[i]);
			}
			uidItems.put(elms[0], items);
			++lineNum;
			if(lineNum%mod==0)System.out.println("read "+lineNum+" line items");
		}
		System.out.println("total:read "+lineNum+" line items");
		return uidItems;
	}

	public static Map<String,Integer> readIdCateMapFromFile(String idCateFile,String seperater) throws IOException{
		Map<String,Integer> idcateMap=new HashMap<String,Integer>();
		BufferedReader reader=FileTool.getBufferedReaderFromFile(idCateFile);
		String line="";
		while((line=reader.readLine())!=null){
			String[] elms=line.split(seperater);
			String id=elms[0].trim();
			Integer cate=Integer.parseInt(elms[1]);
			idcateMap.put(id, cate);
		}
		return idcateMap;
	}

	public static Map<String,List<String>> readKeyItemsMapFromFile(String keyItems,String keyItemsSeperater) throws IOException
	{
		Map<String,List<String>> keyItemsMap=new HashMap<String,List<String>>();
		BufferedReader reader=FileTool.getBufferedReaderFromFile(keyItems);
		String line="";
		while((line=reader.readLine())!=null)
		{
			String[] elms=line.split(keyItemsSeperater);
			if(elms.length<=0)
			{
				continue;
			}
			String key=elms[0].trim();
			List<String> items=keyItemsMap.get(key);
			if(items==null)
			{
				items=new LinkedList<String>();

			}
			for(int i=1;i<elms.length;++i)
			{
				items.add(elms[i]);
			}
			keyItemsMap.put(key, items);
		}
		return keyItemsMap;

	}

	public static void writeFriNumUdatasMap2File(Map<Integer,List<UserDataFull>> friNumUdatas,int writeTotalSize,
			String destUidTags,String destUidTagsSeperater,String destUidFris,String destUidFrisSeperater) throws UnsupportedEncodingException, FileNotFoundException{
		PrintWriter uidTagsWriter=FileTool.getPrintWriterForFile(destUidTags);
		PrintWriter uidFrisWriter=FileTool.getPrintWriterForFile(destUidFris);
		int totalSize=Stat.countMapValueItems(friNumUdatas);

		int offset=0;
		if(writeTotalSize>=0){
			offset=totalSize-writeTotalSize;
		}
		if(totalSize<writeTotalSize){
			System.out.println("wanrn: totalSize<writeTotalSize");
		}
		if(totalSize>=writeTotalSize){
			int i=0;
			for(Map.Entry<Integer,List<UserDataFull>> entry:friNumUdatas.entrySet()){
				List<UserDataFull> udatas=entry.getValue();
				for(UserDataFull udata:udatas){
					if(i>=offset){
						uidTagsWriter.write(udata.uid+destUidTagsSeperater);
						IO.writeStrList(udata.tags, uidTagsWriter, destUidTagsSeperater);

						uidFrisWriter.write(udata.uid+destUidFrisSeperater);
						IO.writeStrList(udata.friends, uidFrisWriter, destUidFrisSeperater);
					}
					++i;
				}
			}
		}

		uidTagsWriter.close();
		uidFrisWriter.close();
	}

	private static void writeStrList(List<String> strList,PrintWriter writer,String seperater){
		for(int i=0;i<strList.size();++i){
			if(i==strList.size()-1){
				writer.write(strList.get(i));
			}else{
				writer.write(strList.get(i)+seperater);
			}
		}
		writer.write("\r\n");
	}

	public static Map<String,String> readWordVecMap(String wordVecFilePath,String seperater) throws IOException
	{
		Map<String,String> wordVecMap=new HashMap<String,String>();
		BufferedReader reader=FileTool.getBufferedReaderFromFile(wordVecFilePath);
		String line="";
		reader.readLine();
		while((line=reader.readLine())!=null)
		{
			String[] elms=line.split(seperater, 2);
			String word=elms[0];
			String vec="";
			if(elms.length>=2)
			{
				vec=elms[1].trim();
			}
			wordVecMap.put(word, vec);
		}
		return wordVecMap;
	}

	public static int readVecDiem(String wordVecFile,String seperater) throws IOException
	{
		BufferedReader reader=FileTool.getBufferedReaderFromFile(wordVecFile);
		String line="";
		reader.readLine();
		line=reader.readLine();
		String[] elms=line.split(seperater);
		return elms.length-1;

	}
	public static Map<String,String> readKeyValueMap(String keyValueFile,String seperater,int ignoreLineNumFromHead) throws IOException
	{
		Map<String,String> map=new HashMap<String,String>();
		BufferedReader reader=FileTool.getBufferedReaderFromFile(keyValueFile);
		String line="";
		
		for(int i=0;i<ignoreLineNumFromHead;++i)
		{
			reader.readLine();
		}
		
		while((line=reader.readLine())!=null)
		{
			if(line.equals(""))
			{
				continue;
			}
			String[] elms=line.split(seperater, 2);
			if(elms.length==1)
			{
				map.put(elms[0], "");
			}
			else
			{
				map.put(elms[0], elms[1]);
			}
		}
		return map;
	}
	public static Map<String,String> readKeyValueMap(String keyValueFile,String seperater) throws IOException
	{
		return readKeyValueMap(keyValueFile,seperater,0);
	}

	public static void writeMap(Map<String,String> map,String seperater,PrintWriter writer)
	{
		for(Map.Entry<String, String> entry:map.entrySet())
		{
			writer.write(entry.getKey()+seperater+entry.getValue()+"\r\n");
		}
		writer.flush();
	}
    public static void writeSet(Set<String> set,PrintWriter writer)
    {
    	for(String s:set)
    	{
    		writer.write(s+"\r\n");
    	}
    	writer.flush();
    }
	public static void writeWordVec(String word,double[] vec,String seperater,PrintWriter writer)
	{
		writeWordVecXTimes(word,vec,1.0f,seperater,writer);
	}
	public static void writeWordVecXTimes(String word,double[] elmentMultiVec,float times,String seperater,PrintWriter writer)
	{
		writer.write(word);
		for(double d:elmentMultiVec)
		{
			writer.write(seperater+(d*times));
		}
		writer.write("\r\n");
		writer.flush();
	}
	
	public static Map<String,List<Double>> readKeyVecMap(String wordVecFilePath,String seperater,int ignoreLineFromhead) throws IOException{
		Map<String,List<Double>> keyVecMap=new LinkedHashMap<String,List<Double>>();
		BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(wordVecFilePath),"utf-8"));
		String line="";
		int lineCount=0;
		while((line=reader.readLine())!=null){
			++lineCount;
			if(lineCount<=ignoreLineFromhead){
				continue;
			}
			String[] elms=line.split(seperater);
			String word=elms[0];
			List<Double> vec=new ArrayList<Double>();
			for(int i=1;i<elms.length;++i){
				vec.add(Double.parseDouble(elms[i]));
			}
			keyVecMap.put(word, vec);
		}
		return keyVecMap;
	}
	public static Map<String,double[]> readKeyVecMapV1(String wordVecFilePath,String seperater,int ignoreLineFromhead) throws IOException{
		Map<String,double[]> keyVecMap=new LinkedHashMap<String,double[]>();
		BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(wordVecFilePath),"utf-8"));
		String line="";
		int lineCount=0;
		while((line=reader.readLine())!=null){
			++lineCount;
			if(lineCount<=ignoreLineFromhead){
				continue;
			}
			String[] elms=line.split(seperater);
			String word=elms[0];
			double[] vec=new double[elms.length-1];
			for(int i=1;i<elms.length;++i){
				vec[i-1]=Double.parseDouble(elms[i]);
			}
			keyVecMap.put(word, vec);
		}
		return keyVecMap;
	}
	

	public static Set<String> readUids(String uids) throws IOException{
		
		return IO.readUids(uids,"\t");
	}
	public static Set<String> readUids(String uids,String uidSeperater) throws IOException{
		BufferedReader reader=FileTool.getBufferedReaderFromFile(uids);
		Set<String> ids=new HashSet<String>();
		String line="";
		while((line=reader.readLine())!=null){
			String[] elms=line.split(uidSeperater);
			String id=elms[0].trim();
			ids.add(id);
		}
		return ids;
	}
	public static Set<String> readWeiboIdSetFromUidWeiboIds(String uidWeiboIds,String uidWeiboIdsSeperater) throws IOException
	{
		BufferedReader reader=FileTool.getBufferedReaderFromFile(uidWeiboIds);
		Set<String> weiboIds=new HashSet<String>();
		String line="";
		while((line=reader.readLine())!=null){
			if(line.equals(""))
			{
				continue;
			}
			String[] elms=line.split(uidWeiboIdsSeperater);
			for(int i=1;i<elms.length;++i)
			{
				weiboIds.add(elms[i].trim());
			}
		}
		return weiboIds;
	}
	
	public static void readUidLabel(String uids,String label,Map<String,String> idLabelMap) throws IOException{
		BufferedReader reader=FileTool.getBufferedReaderFromFile(uids);
		String line="";
		while((line=reader.readLine())!=null){
			String[] elms=line.split("\t");
			String id=elms[0].trim();
			idLabelMap.put(id, label);
		}
	}
	public static void writeWordVec(String word,List<Double> vec,String seperater,PrintWriter writer)
	{
		writeWordVecXTimes(word,vec,1.0f,seperater,writer);
	}
	public static void writeWordVecXTimes(String word,List<Double> vec,float times,String seperater,PrintWriter writer)
	{
		writer.write(word);
		for(double d:vec)
		{
			writer.write(seperater+(d*times));
		}
		writer.write("\r\n");
		writer.flush();
	}
	
	public static Set<String> readIdSetFromReview(String idFrisReview,String itemSeperater,String innerSeperater) throws IOException
	{
		Set<String> idSet=new HashSet<String>();
		BufferedReader reader=FileTool.getBufferedReaderFromFile(idFrisReview);
		String line="";
		while((line=reader.readLine())!=null)
		{
			if(line.equals(""))
			{
				continue;
			}
			String[] elms=line.split(itemSeperater);
			String uid=elms[0].trim();
			idSet.add(uid);
			if(elms.length>3)
			{
				String[] fris=elms[3].split(innerSeperater);
				for(String f:fris)
				{
					idSet.add(f.trim());
				}
			}
		}
		return idSet;
		
	}
	


}
