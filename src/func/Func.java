package func;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.sf.json.JSONObject;
import utils.FileTool;
import utils.IO;
import utils.MyToolKit;

public class Func {
	static String url="";
	static String email="";

	public static void json2Weibos(String jsonDataDir,String weibosFilePath,String seperater) throws IOException{
		File dir=new File(jsonDataDir);
		File[] files=dir.listFiles();
		PrintWriter writer=new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(weibosFilePath),"utf-8")));
		for(File f: files){
			BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(f),"utf-8"));
			String jsonLine="";
			StringBuilder weibo=new StringBuilder();
			String id=f.getName().trim().split("\\.")[0];
			int lineNum=0;
			while((jsonLine=reader.readLine())!=null){
				JSONObject jobj=JSONObject.fromObject(jsonLine);
				JSONObject weiboJobj=(JSONObject) jobj.get("weibo");
				String oneWeibo=weiboJobj.getString("text");
				weibo.append(oneWeibo);
				weibo.append(seperater);
				++lineNum;
				//if(lineNum>=3) break;
			}
			writer.write(id+seperater+weibo.toString()+"\r\n");
		}
		writer.close();
	}

	public static void split(String filePath,String seperater,int splitPos,String file1,String file2) throws IOException{
		BufferedReader reader=new BufferedReader(new InputStreamReader(new FileInputStream(filePath),"utf-8"));
		PrintWriter writer1=new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file1),"utf-8")));
		PrintWriter writer2=new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file2),"utf-8")));
		String line="";
		while((line=reader.readLine())!=null){
			String[] elms=line.split(seperater, 2);
			writer1.write(elms[0]+"\r\n");
			writer2.write(elms[1]+"\r\n");
		}
		writer1.close();
		writer2.close();
	}

	public static void mergeUidAndWords(String uid,String words,String dest,String seperater) throws IOException{
		PrintWriter writer=new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dest),"utf-8")));
		BufferedReader readerUid=new BufferedReader(new InputStreamReader(new FileInputStream(uid),"utf-8"));
		BufferedReader readerWord=new BufferedReader(new InputStreamReader(new FileInputStream(words),"utf-8"));
		String uidLine="";
		String wordsLine="";
		StringBuilder weibo=new StringBuilder();
		while((uidLine=readerUid.readLine())!=null&&(wordsLine=readerWord.readLine())!=null){
			writer.write(uidLine+seperater+wordsLine+"\r\n");
		}
		writer.close();
	}
	public static void oneWeiboOneLine(String weiboFilePath,String weiboSeperater,String oneWeiboOneLineFilePath) throws IOException{
		BufferedReader reader=FileTool.getBufferedReaderFromFile(weiboFilePath);
		PrintWriter writer=FileTool.getPrintWriterForFile(oneWeiboOneLineFilePath);

		String line="";
		while((line=reader.readLine())!=null){
			String[] elms=line.split(weiboSeperater);
			for(String s:elms){
				writer.write(s+"\r\n");
			}
		}

		writer.close();
	}

	public static void uidWeibos2uidWeiboId(String uidWeibos,String uidWeibosSeperater,String uidWeiboIds,String uidWeiboIdsSeperater,String weiboIdAndWeibos) throws IOException{
		BufferedReader reader=FileTool.getBufferedReaderFromFile(uidWeibos);
		PrintWriter uidWeiboIdsWriter=FileTool.getPrintWriterForFile(uidWeiboIds);
		//PrintWriter weiboIdAndWeibosWriter=FileTool.getPrintWriterForFile(FileTool.getParentPath(uidWeiboIds)+"\\weiboIdAndWeibos.txt");
		PrintWriter weiboIdAndWeibosWriter=FileTool.getPrintWriterForFile(weiboIdAndWeibos);

		
		String line="";
		while((line=reader.readLine())!=null){
			String[] elms=line.split(uidWeibosSeperater);
			String uid=elms[0].trim();
			uidWeiboIdsWriter.write(uid);
			for(int i=1;i<elms.length;++i){
				String weiboId=uid+"_"+i;
				uidWeiboIdsWriter.write(uidWeiboIdsSeperater+weiboId);
				weiboIdAndWeibosWriter.write(weiboId+uidWeiboIdsSeperater+elms[i]+"\r\n");
			}
			uidWeiboIdsWriter.write("\r\n");
		}
		uidWeiboIdsWriter.close();
		weiboIdAndWeibosWriter.close();

	}

	public static void uidWeiboId2ReviewForm(String femalIds,String maleIds,String uidWeiboIds,String uidWeiboIdsSeperater,
			String reviewFormFile,String seperater,int weiboIdsNum) throws IOException{
		Map<String,String> idLabelMap=new HashMap<String,String>();
		IO.readUidLabel(femalIds, "1", idLabelMap);
		IO.readUidLabel(maleIds, "2", idLabelMap);
		BufferedReader reader=FileTool.getBufferedReaderFromFile(uidWeiboIds);
		PrintWriter writer=FileTool.getPrintWriterForFile(reviewFormFile);
		String line="";
		while((line=reader.readLine())!=null){
			String[] elms=line.split(uidWeiboIdsSeperater);
			String uid=elms[0].trim();
			String label=idLabelMap.get(uid);
			int weiboIdsSize=elms.length-1;
			if(weiboIdsNum>0&&weiboIdsNum<elms.length-1){
				weiboIdsSize=weiboIdsNum;
			}
			StringBuilder stb=new StringBuilder();
			for(int i=0;i<weiboIdsSize;++i){
				if(i!=weiboIdsSize-1){
					stb.append(elms[i+1]+uidWeiboIdsSeperater);
				}else{
					stb.append(elms[i+1]);
				}
			}
			String weiboIds=stb.toString();
			writer.write(uid+seperater+"Weibos"+seperater+label+seperater+weiboIds);
			writer.write("\r\n");
		}
		writer.close();

	}
	public static void uidWeiboId2ReviewFormV2(String allIdCate,String allIdCateSeperater,String uidWeiboIds,String uidWeiboIdsSeperater,
			String reviewFormFile,String seperater,int weiboIdsNum) throws IOException
	{
		Map<String,String> idLabelMap=IO.readKeyValueMap(allIdCate, allIdCateSeperater);
		BufferedReader reader=FileTool.getBufferedReaderFromFile(uidWeiboIds);
		PrintWriter writer=FileTool.getPrintWriterForFile(reviewFormFile);
		String line="";
		while((line=reader.readLine())!=null){
			String[] elms=line.split(uidWeiboIdsSeperater);
			String uid=elms[0].trim();
			String label=idLabelMap.get(uid);
			int weiboIdsSize=elms.length-1;
			if(weiboIdsNum>0&&weiboIdsNum<elms.length-1){
				weiboIdsSize=weiboIdsNum;
			}
			StringBuilder stb=new StringBuilder();
			for(int i=0;i<weiboIdsSize;++i){
				if(i!=weiboIdsSize-1){
					stb.append(elms[i+1]+uidWeiboIdsSeperater);
				}else{
					stb.append(elms[i+1]);
				}
			}
			String weiboIds=stb.toString();
			writer.write(uid+seperater+"Weibos"+seperater+label+seperater+weiboIds);
			writer.write("\r\n");
		}
		writer.close();
	}
	
	public static float random(Random r,float min,float max){
		return r.nextFloat()*(max-min)+min;
	}
	public static List<Double> avgVec(List<String> words,Map<String,List<Double>> vecMat){
		List<Double> avgVec=new ArrayList<Double>();
		int count=0;
		for(String word:words){
			List<Double> vec=vecMat.get(word);
			if(vec!=null){
				for(int i=0;i<vec.size();++i){
					if(avgVec.size()<vec.size()){
						avgVec.add(vec.get(i));
					}else{
						avgVec.set(i, avgVec.get(i)+vec.get(i));
					}
				}
				++count;
			}
		}
		for(int i=0;i<avgVec.size();++i){
			avgVec.set(i, avgVec.get(i)/(count*1.0));
		}
		return avgVec;
	}
	public static List<List<String>> foldArrays(String[] arrys,int foldEleNum){
		List<List<String>> folds=new ArrayList<List<String>>();
		int completeFilledFoldNum=arrys.length/foldEleNum;
		int k=0;
		for(int i=0;i<arrys.length;++i){
			if(k<=completeFilledFoldNum&&i<k*foldEleNum){
				List<String> fold=folds.get(k-1);
				fold.add(arrys[i]);
			}
			if(k<=completeFilledFoldNum&&i==k*foldEleNum){
				List<String> fold=new ArrayList<String>();
				fold.add(arrys[i]);
				folds.add(fold);
				++k;
			}
			if(k>completeFilledFoldNum&&i!=(k-1)*foldEleNum){
				List<String> fold=folds.get(k-1);
				fold.add(arrys[i]);
			}
		}
		return folds;
	}
	public static void avgWeiboVec(String source,String weiboId_vecFile,int avgNum,String dest,String dest_avgWeibo_vec) throws IOException{
		BufferedReader reader=FileTool.getBufferedReaderFromFile(source);
		PrintWriter writerReview=FileTool.getPrintWriterForFile(dest);
		PrintWriter writerVec=FileTool.getPrintWriterForFile(dest_avgWeibo_vec);
		Map<String,List<Double>> vecMat=IO.readKeyVecMap(weiboId_vecFile, " ", 1);

		String line="";
		String seperater1="\t\t";
		String seperater2="\t";
		int newVecCount=0;
		while((line=reader.readLine())!=null){
			String[] elms=line.split(seperater1);

			String uid=elms[0].trim();
			String type=elms[1];
			String label=elms[2];
			String weiboIds=elms[3];
			String[] elmsWeiboId=weiboIds.split(seperater2);

			List<String> newIds=new ArrayList<String>();
			List<Double> avgVec=null;
			List<String> avgWeiboIds=null;


			List<List<String>> folds=foldArrays(elmsWeiboId,avgNum);
			for(List<String> fold:folds){
				String newId=fold.get(0)+"_"+fold.size();
				newIds.add(newId);
				avgVec=Func.avgVec(fold, vecMat);

				++newVecCount;
				if(newVecCount==1){
					writerVec.write("X "+avgVec.size()+"\r\n");
					writerVec.write("</s>");
					Random r=new Random();
					for(int j=0;j<avgVec.size();++j){
						writerVec.write(" "+random(r,-1.0f,1.0f));
					}
					writerVec.write("\r\n");
				}

				writerVec.write(newId);
				for(Double d:avgVec){
					writerVec.write(" "+d);
				}
				writerVec.write("\r\n");
			}
			writerReview.write(uid+seperater1+type+seperater1+label+seperater1);
			for(int i=0;i<newIds.size();++i){
				if(i==newIds.size()-1){
					writerReview.write(newIds.get(i));
				}else{
					writerReview.write(newIds.get(i)+seperater2);
				}
			}
			writerReview.write("\r\n");
		}
		writerReview.close();
		writerVec.close();
	}

	public static void extractNumWeibo(String reviews,String dest,int num) throws IOException{
		BufferedReader reader=FileTool.getBufferedReaderFromFile(reviews);
		PrintWriter writer=FileTool.getPrintWriterForFile(dest);

		String line="";
		String seperater1="\t\t";
		String seperater2="\t";
		while((line=reader.readLine())!=null){
			String[] elms=line.split(seperater1);

			String uid=elms[0].trim();
			String type=elms[1];
			String label=elms[2];
			String weiboIds=elms[3];
			String[] elmsWeiboId=weiboIds.split(seperater2);
			StringBuilder stb=new StringBuilder();
			int size=num;
			if(num>elmsWeiboId.length)
				size=elmsWeiboId.length;
			for(int i=0;i<size;++i){
				if(i==num-1){
					stb.append(elmsWeiboId[i]);
				}else{
					stb.append(elmsWeiboId[i]+seperater2);
				}
			}
			writer.write(uid+seperater1+type+seperater1+label+seperater1+stb.toString()+"\r\n");
		}
		writer.close();
	}

	public static void extractNumWeiboLineForm(String reviews,String dest,int num) throws IOException{
		BufferedReader reader=FileTool.getBufferedReaderFromFile(reviews);
		PrintWriter writer=FileTool.getPrintWriterForFile(dest);

		String line="";
		String seperater1="\t\t";
		String seperater2="\t";
		while((line=reader.readLine())!=null){
			String[] elms=line.split(seperater1);

			String uid=elms[0].trim();
			String type=elms[1];
			String label=elms[2];
			
			String weiboIds=elms[3];
			String[] elmsWeiboId=weiboIds.split(seperater2);
			StringBuilder stb=new StringBuilder();
			int size=num;
			if(num>elmsWeiboId.length)
				size=elmsWeiboId.length;
			for(int i=0;i<size;++i){
				if(i==num-1){
					stb.append(elmsWeiboId[i]);
				}else{
					stb.append(elmsWeiboId[i]+seperater2);
				}
			}
			writer.write(uid+seperater1+type+seperater1+label+seperater1+stb.toString()+"\r\n");
		}
		writer.close();
	}
}
