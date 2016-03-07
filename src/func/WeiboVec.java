package func;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WeiboVec {
	private String wordVecInfo="";
	private Map<String,List<Double>> wordVecMatrix;
	public Map<String,List<Double>> loadWordVecMaxtrix(String wordVecFilePath,String seperater) throws IOException{
		this.wordVecMatrix=new LinkedHashMap<String,List<Double>>();
		BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(wordVecFilePath),"utf-8"));
		String line="";
		int lineCount=0;
		while((line=reader.readLine())!=null){
			++lineCount;
			if(lineCount==1){
				wordVecInfo=line;
				continue;
			}
			String[] elms=line.split(seperater);
			String word=elms[0];
			List<Double> vec=new ArrayList<Double>();
			for(int i=1;i<elms.length;++i){
				vec.add(Double.parseDouble(elms[i]));
			}
			this.wordVecMatrix.put(word, vec);
		}
		return this.wordVecMatrix;
	}

	public void generateWeiboVecAvg(String idWordFilePath,String idWordSeperater,
			String weioboVecFilePath,String weiboVecSeperater) throws IOException{
		BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(idWordFilePath),"utf-8"));
		PrintWriter writer=new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(weioboVecFilePath),"utf-8")));

		String line="";

		writer.write(wordVecInfo+"\r\n");
		writer.write("</s>");
		for(Double value:wordVecMatrix.get("</s>")){
			writer.write(weiboVecSeperater+value);
		}
		writer.write("\r\n");
		
		while((line=reader.readLine())!=null){
			String[] elms=line.split(idWordSeperater);
			String id=elms[0];
			List<Double> avg=new ArrayList<Double>();
			int totalnum=0;
			for(int i=1;i<elms.length;++i){
				List<Double> vec=this.wordVecMatrix.get(elms[i]);
				if(vec!=null){
					if(avg.size()==0){
						for(int k=0;k<vec.size();++k){
							avg.add(vec.get(k));
						}
					}
					else{
						for(int k=0;k<vec.size();++k){
							avg.set(k, avg.get(k)+vec.get(k));
						}
					}
					++totalnum;
				}else{
					System.out.println(id+" "+i+" elment "+elms[i] +" no vec");
				}
			}

			for(int k=0;k<avg.size();++k){
				avg.set(k, avg.get(k)/totalnum);
			}

			writer.write(id);
			for(Double value:avg){
				writer.write(weiboVecSeperater+value);
			}
			writer.write("\r\n");
		}
		writer.close();
	}

}
