package func;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import utils.FileTool;

public class UserVec {
	private Map<String,List<Double>> wordVecMatrix;
	private Map<String,String> idsLabel;
	private Map<String,String> trainMap;
	private Map<String,String> testMap;

	public Map<String,String> loadIdsLabel(String ...strings) throws IOException{
		this.idsLabel=new LinkedHashMap<String,String>();
		int count=0;
		for(String s:strings){
			BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(s),"utf-8"));
			String line="";
			String label="1";
			if(count==1) label="2";
			while((line=reader.readLine())!=null){
				String[] elms=line.split("\t");
				String id=elms[0].trim();
				this.idsLabel.put(id, label);
			}
			++count;
		}
		return this.idsLabel;
	}
	public Map<String,List<Double>> loadWordVecMaxtrix(String wordVecFilePath,String seperater) throws IOException{
		this.wordVecMatrix=new LinkedHashMap<String,List<Double>>();
		BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(wordVecFilePath),"utf-8"));
		String line="";
		while((line=reader.readLine())!=null){
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

	public void generateUserVecAvg(String uidWordFilePath,String uidWordSeperater,
			String userVecFilePath,String userVecSeperater) throws IOException{
		BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(uidWordFilePath),"utf-8"));
		PrintWriter writer=new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(userVecFilePath),"utf-8")));

		String line="";
		while((line=reader.readLine())!=null){
			String[] elms=line.split(uidWordSeperater);
			String uid=elms[0];
			String label=this.idsLabel.get(uid);
			List<Double> avg=new ArrayList<Double>();
			for(int i=1;i<elms.length;++i){
				List<Double> vec=this.wordVecMatrix.get(elms[i]);
				if(vec!=null){
					if(1==i){
						for(int k=0;k<vec.size();++k){
							avg.add(vec.get(k));
						}
					}
					else{
						for(int k=0;k<vec.size();++k){
							avg.set(k, avg.get(k)+vec.get(k));
						}
					}
				}else{
					//					System.out.println(elms[i] +" no vec");
				}
			}
			int totalnum=elms.length-1;
			for(int k=0;k<avg.size();++k){
				avg.set(k, avg.get(k)/totalnum);
			}

			writer.write(uid+userVecSeperater+label);
			for(Double value:avg){
				writer.write(userVecSeperater+value);
			}
			writer.write("\r\n");
		}
		writer.close();
	}

	public void generateUserVecAvgLineForm(String uidWordFilePath,String uidWordSeperater,
			String userVecFilePath,String userVecSeperater) throws IOException{
		BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(uidWordFilePath),"utf-8"));
		PrintWriter writer=new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(userVecFilePath),"utf-8")));

		String line="";
		while((line=reader.readLine())!=null){
			String[] elms=line.split(uidWordSeperater);
			String uid=elms[0];
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
					totalnum++;
				}else{
					System.out.println(elms[i] +" no vec");
				}
			}
			
			for(int k=0;k<avg.size();++k){
				avg.set(k, avg.get(k)/totalnum);
			}

			writer.write(uid);
			for(int i=0;i<avg.size();++i){
				writer.write(userVecSeperater+(i+1)+":"+avg.get(i));
			}
			writer.write("\r\n");
		}
		writer.close();
	}


	protected void setTrainAndTestMap(float trainPart){
		Map<String,List<String>> labelIds=new LinkedHashMap<String,List<String>> ();
		for(Map.Entry<String, String> entry:this.idsLabel.entrySet()){
			if(!labelIds.containsKey(entry.getValue())){
				List<String> ids=new ArrayList<String>();
				ids.add(entry.getKey());
				labelIds.put(entry.getValue(), ids);
			}else{
				List<String> ids=labelIds.get(entry.getValue());
				ids.add(entry.getKey());
				labelIds.put(entry.getValue(), ids);
			}
		}
		Map<String,String> tarinMap=new LinkedHashMap<String,String>();
		Map<String,String> testMap=new LinkedHashMap<String,String>();
		for(Map.Entry<String, List<String>> entry:labelIds.entrySet()){
			List<String> ids=entry.getValue();
			for(int i=0;i<ids.size();++i){
				if(i<(ids.size()*trainPart)){
					tarinMap.put(ids.get(i), entry.getKey());
				}else{
					testMap.put(ids.get(i), entry.getKey());
				}
			}
		}
		this.trainMap=tarinMap;
		this.testMap=testMap;
	}
	public void generateTrainAndTest(String uidLableVecFile,String seperater,float trainPart,String trainFile,String testFile) throws IOException{
		BufferedReader reader=FileTool.getBufferedReaderFromFile(uidLableVecFile);
		PrintWriter writerTrain=FileTool.getPrintWriterForFile(trainFile);
		PrintWriter writerTest=FileTool.getPrintWriterForFile(testFile);

		this.setTrainAndTestMap(trainPart);
		String line="";
		PrintWriter writer=writerTrain;
		Map<String,String> map=new TreeMap<String,String>();
		while((line=reader.readLine())!=null){
			String[] elms=line.split(seperater, 3);
			String id=elms[0];
			map.put(id, line);
		}

		for(Map.Entry<String, String> entry:map.entrySet()){
			String id=entry.getKey();
			if(trainMap.containsKey(id)){
				writer=writerTrain;
			}
			if(testMap.containsKey(id)){
				writer=writerTest;
			}
			writer.write(entry.getValue()+"\r\n");
		}
		writerTrain.close();
		writerTest.close();

	}
	public void generateSortIdLableVec(String uidLableVecFile,String seperater,String sortFile) throws IOException{
		BufferedReader reader=FileTool.getBufferedReaderFromFile(uidLableVecFile);
		PrintWriter writer=FileTool.getPrintWriterForFile(sortFile);
		String line="";
		Map<String,String> map=new TreeMap<String,String>();
		while((line=reader.readLine())!=null){
			String[] elms=line.split(seperater, 3);
			String id=elms[0];
			map.put(id, line);
		}

		for(Map.Entry<String, String> entry:map.entrySet()){
			writer.write(entry.getValue()+"\r\n");
		}

		writer.close();
	}

	public void generateSortIdSourceLabelWords(String uidWordsFc,String seperater,String sourceName,String sortFile,String sortSeperater) throws IOException{
		BufferedReader reader=FileTool.getBufferedReaderFromFile(uidWordsFc);
		PrintWriter writer=FileTool.getPrintWriterForFile(sortFile);
		String line="";
		Map<String,String> map=new TreeMap<String,String>();
		while((line=reader.readLine())!=null){
			String[] elms=line.split(seperater, 2);
			String id=elms[0];
			map.put(id, elms[1]);
		}

		for(Map.Entry<String, String> entry:map.entrySet()){
			String id=entry.getKey();
			writer.write(id+sortSeperater+sourceName+sortSeperater+this.idsLabel.get(id)+sortSeperater+entry.getValue()+"\r\n");
		}

		writer.close();
	}
	public void generateSortIdSourceLabelWordsTrainAndTest(String uidWordsFc,String seperater,float trainPart,
			String sourceName,String trainFile,String testFile,String sortSeperater) throws IOException{
		BufferedReader reader=FileTool.getBufferedReaderFromFile(uidWordsFc);
		PrintWriter writerTrain=FileTool.getPrintWriterForFile(trainFile);
		PrintWriter writerTest=FileTool.getPrintWriterForFile(testFile);

		this.setTrainAndTestMap(trainPart);
		String line="";
		PrintWriter writer=writerTrain;
		Map<String,String> map=new TreeMap<String,String>();
		while((line=reader.readLine())!=null){
			String[] elms=line.split(seperater, 2);
			String id=elms[0];
			map.put(id, elms[1]);
		}

		for(Map.Entry<String, String> entry:map.entrySet()){
			String id=entry.getKey();
			if(trainMap.containsKey(id)){
				writer=writerTrain;
			}
			if(testMap.containsKey(id)){
				writer=writerTest;
			}
			writer.write(id+sortSeperater+sourceName+sortSeperater+this.idsLabel.get(id)+sortSeperater+entry.getValue()+"\r\n");
		}
		writerTrain.close();
		writerTest.close();
	}
}
