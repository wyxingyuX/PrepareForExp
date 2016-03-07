package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class TestTool {
	public static  void print(Object o){
		System.out.print(o);

	}
	public static  void println(Object o){
		System.out.println(o);
	}
	
	public static void  printCateNum(String idaCates,String seperater) throws IOException{
		Map<Integer,Integer> cateNumMap=new HashMap<Integer,Integer>();
		BufferedReader reader=FileTool.getBufferedReaderFromFile(idaCates);
		String line="";
		while((line=reader.readLine())!=null){
			String[] elms=line.split(seperater);
			String id=elms[0].trim();
			int cate=Integer.parseInt(elms[1]);
			Integer count=cateNumMap.get(cate);
			if(count==null){
				cateNumMap.put(cate, 1);
			}else{
				cateNumMap.put(cate, count+1);
			}
		}
		
		for(Map.Entry<Integer, Integer> entry:cateNumMap.entrySet()){
			System.out.println(entry.getKey()+" category : "+entry.getValue()+" user");
		}
	}
	
}
