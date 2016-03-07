package func;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import Bean.SKLE.Item;
import Bean.SKLE.User;
import net.sf.json.JSONObject;
import utils.FileTool;
import utils.IO;
import utils.MyMathTool;
import utils.MyToolKit;

public class ExtractData {

	public static void extractUidTags(List<String> userInfoFileList,String dest,String destSeperater) throws IOException{
		PrintWriter writer=FileTool.getPrintWriterForFile(dest);
		for(String uinfoFile:userInfoFileList){
			BufferedReader reader=FileTool.getBufferedReaderFromFile(uinfoFile);
			String line="";
			while((line=reader.readLine())!=null){
				String uid=ExtractData.getJsonLineValueOf("id", line);
				writer.write(uid);
				List<String> tags=ExtractData.getTagsFromJsonLine(line);
				for(String tag:tags){
					writer.write(destSeperater+tag);
				}
				writer.write("\r\n");
			}
		}

		writer.close();
	}

	public static void extractUidFrids(List<String> userInfoFileList,String dest,String destSeperater) throws IOException{
		PrintWriter writer=FileTool.getPrintWriterForFile(dest);
		for(String uinfoFile:userInfoFileList){
			BufferedReader reader=FileTool.getBufferedReaderFromFile(uinfoFile);
			String line="";
			while((line=reader.readLine())!=null){
				String uid=ExtractData.getJsonLineValueOf("id", line);
				writer.write(uid);
				List<String> fids=ExtractData.getFriIdsFromJsonLine(line);
				for(String fid:fids){
					writer.write(destSeperater+fid);
				}
				writer.write("\r\n");
			}
		}

		writer.close();
	}

	public static void extractSelectedUidItems(String uidsFile,String uidsSeperater,
			String allUidItemsFile,String allUidItemsSeperater,String dest,String destSeperrater) throws IOException{
		Map<String,List<String>> uidItemsMap=IO.readUidItems(allUidItemsFile, allUidItemsSeperater);
		BufferedReader reader=FileTool.getBufferedReaderFromFile(uidsFile);
		PrintWriter writer=FileTool.getPrintWriterForFile(dest);
		String line="";
		while((line=reader.readLine())!=null){
			String[] elms=line.split(uidsSeperater);
			String uid=elms[0].trim();
			writer.write(uid+destSeperrater);
			List<String> items=uidItemsMap.get(uid);
			if(items==null){
				System.out.println(allUidItemsFile+" no "+uid);
			}else{
				writer.write(MyToolKit.strList2concateStr(items, destSeperrater));
			}
			writer.write("\r\n");
		}
		writer.close();
	}

	//if maxTagNum  is negative number ,that mean infinit number
	public static void extractFrisFromUidFrisSatisfy(String uidFris,
			String uidFrisSeperater,
			String allUidTags,
			String allUidTagsSeperater,
			int minTagNum,
			int maxTagNum,
			String destSatisfyUidFris,
			String destUidFrisSeperater) throws Exception
	{
		Map<String,List<String>> allUidTagsMap=IO.readUidTagsMapFromFile(allUidTags, allUidTagsSeperater);
		BufferedReader reader=FileTool.getBufferedReaderFromFile(uidFris);
		PrintWriter writer=FileTool.getPrintWriterForFile(destSatisfyUidFris);

		String line="";
		while((line=reader.readLine())!=null)
		{
			String[] elms=line.split(uidFrisSeperater);
			if(elms.length<=0)
			{
				continue;
			}
			String uid=elms[0].trim();
			writer.write(uid);
			for(int i=1;i<elms.length;++i)
			{
				String friId=elms[i].trim();
				if(isfriTagNumSatisfy(friId,allUidTagsMap,minTagNum,maxTagNum))
				{
					writer.write(destUidFrisSeperater+friId);
				}
			}
			writer.write("\r\n");
		}
		writer.close();
	}

	private static boolean isfriTagNumSatisfy(String friId,Map<String,List<String>> uidTagsMap,int minTagNum,int maxTagNum) throws Exception
	{
		List<String> friTags=uidTagsMap.get(friId);
		if(friTags==null)
		{
			return false;
		}
		int friTagsNum=friTags.size();
		if(minTagNum<0)
		{
			minTagNum=0;
		}
		if(maxTagNum>=0&&minTagNum>maxTagNum)
		{
			throw new Exception("Error:minTagNum>maxTagNum");
		}

		if(maxTagNum>=0&&maxTagNum>=minTagNum&&friTagsNum>=minTagNum&&friTagsNum<maxTagNum)
		{
			return true;
		}
		if(maxTagNum<0&&friTagsNum>=minTagNum)
		{
			return true;
		}
		return false;
	}

	public static void generateUidTagsAddFriTgsReviews(String uidFrisReview,String uidFrisReviewSeperater,String reviewWordsSeperater
			,String uidTags,String uidTagsSeperater,String destUidTagsAddFriTagsReview,String destUidTagsAddFriTags) throws IOException
	{
		Map<String,List<String>> idTagsMap=IO.readUidTagsMapFromFile(uidTags, uidTagsSeperater);
		BufferedReader reader=FileTool.getBufferedReaderFromFile(uidFrisReview);
		PrintWriter writerReview=FileTool.getPrintWriterForFile(destUidTagsAddFriTagsReview);
		PrintWriter writerTags=FileTool.getPrintWriterForFile(destUidTagsAddFriTags);
		String line="";
		int lineNum=0;

		while((line=reader.readLine())!=null)
		{
			++lineNum;
			String[] elms=line.split(uidFrisReviewSeperater);
			if(elms.length<=1)
			{
				System.out.println("Warn:"+uidFrisReview+" line "+lineNum+" miss some item");
				continue;
			}
			String uid=elms[0].trim();
			String src=elms[1];
			String cate=elms[2];
			writerReview.write(uid+uidFrisReviewSeperater+src+uidFrisReviewSeperater+cate+uidFrisReviewSeperater);

			StringBuilder tagsStb=new StringBuilder();
			List<String> myTags=idTagsMap.get(uid);
			if(myTags==null)
			{
				System.out.println(uid+" no tags");
				continue;
			}
			for(String tag:myTags)
			{
				tagsStb.append(tag+reviewWordsSeperater);
			}


			String frisStr=elms[3];
			if(frisStr!=null&&!frisStr.equals(""))
			{
				String[] fris=frisStr.split(reviewWordsSeperater);
				for(int i=0;i<fris.length;++i)
				{
					String friId=fris[i];
					List<String> friTags=idTagsMap.get(friId);
					if(friTags==null)
					{
						System.out.println(uid+"'s friend "+friId+" no tags");
						continue;
					}
					for(String tag:friTags)
					{
						tagsStb.append(tag+reviewWordsSeperater);
					}
				}
			}

			writerReview.write(tagsStb.toString()+"\r\n");
			writerTags.write(tagsStb.toString()+"\r\n");

		}
		writerReview.close();
		writerTags.close();
	}

	public static void generateUididAddFriIdsReviews(String uidFrisReview,String uidFrisReviewSeperater,String reviewWordsSeperater
			,String destUidIdAddFriIdsReview) throws IOException
	{
		BufferedReader reader=FileTool.getBufferedReaderFromFile(uidFrisReview);
		PrintWriter writerReview=FileTool.getPrintWriterForFile(destUidIdAddFriIdsReview);
		String line="";
		int lineNum=0;

		while((line=reader.readLine())!=null)
		{
			++lineNum;
			String[] elms=line.split(uidFrisReviewSeperater);
			if(elms.length<=1)
			{
				System.out.println("Warn:"+uidFrisReview+" line "+lineNum+" miss some item");
				continue;
			}
			String uid=elms[0].trim();
			String src=elms[1];
			String cate=elms[2];
			writerReview.write(uid+uidFrisReviewSeperater+src+uidFrisReviewSeperater+cate+uidFrisReviewSeperater);

			StringBuilder idsStb=new StringBuilder();

			idsStb.append(uid+reviewWordsSeperater);


			String frisStr=elms[3];
			idsStb.append(frisStr);

			writerReview.write(idsStb.toString()+"\r\n");

		}
		writerReview.close();
	}

	public static void generateSamllIdSetCate(String allIdCateFile,String allIdSeperater,
			String smallIdSetFile,String smallIdSetSeperater,String dest,String destSeperater) throws IOException{
		Map<String,Integer> idcateMap=IO.readIdCateMapFromFile(allIdCateFile, allIdSeperater);
		PrintWriter writer=FileTool.getPrintWriterForFile(dest);
		BufferedReader reader=FileTool.getBufferedReaderFromFile(smallIdSetFile);
		String line="";
		while((line=reader.readLine())!=null){
			String[] elms=line.split(smallIdSetSeperater, 2);
			String id=elms[0].trim();
			writer.write(id+destSeperater+idcateMap.get(id)+"\r\n");
		}   
		writer.close();
	}

	public static void generateUidItemsReviews(String allUidItems,String allUidItemsSeperater,
			String idcate,String idcateSeperater,String dest) throws IOException{
		Map<String,List<String>> uidItemsMap=IO.readUidItems(allUidItems, allUidItemsSeperater);
		PrintWriter writer=FileTool.getPrintWriterForFile(dest);
		BufferedReader reader=FileTool.getBufferedReaderFromFile(idcate);
		String line="";
		String ddtab="\t\t";
		while((line=reader.readLine())!=null){
			String[] elms=line.split(idcateSeperater);
			String uid=elms[0].trim();
			String cate=elms[1];
			writer.write(uid+ddtab+"Tag"+ddtab+cate+ddtab);

			List<String> items=uidItemsMap.get(uid);
			//System.out.println(uid);
			for(int i=0;i<items.size();++i){
				if(i==items.size()-1){
					writer.write(items.get(i));
				}else{
					writer.write(items.get(i)+"\t");
				}
			}

			writer.write("\r\n");
		}

		writer.close();
	}
	public static void generateUidTags(String uids,String uidSeperater,String allUidTags,String allUidTagSeperater,String destUidTag,String destSeperater) throws Exception 
	{
		Map<String,List<String>> allUidTagMap=IO.readUidTagsMapFromFile(allUidTags, allUidTagSeperater);
		BufferedReader reader=FileTool.getBufferedReaderFromFile(uids);
		PrintWriter writer=FileTool.getPrintWriterForFile(destUidTag);
		String line="";
		while((line=reader.readLine())!=null)
		{
			String[] elms=line.split(uidSeperater,2);
			String id=elms[0].trim();
			writer.write(id);
			List<String> tags=allUidTagMap.get(id);
			if(tags!=null)
			{
				for(String tag:tags)
				{
					writer.write(destSeperater+tag);
				}
			}
			writer.write("\r\n");	
		}
		writer.close();

	}

	public  static void extractUidFriIdTags(String uidFriIds,String uidFriIdsSeperater
			,String allUidTags,String allUidTagsSeperater,String dest,String destSeperater) throws IOException{
		Map<String,List<String>> uidTags=IO.readUidItems(allUidTags,allUidTagsSeperater);

		BufferedReader reader=FileTool.getBufferedReaderFromFile(uidFriIds);
		PrintWriter writer=FileTool.getPrintWriterForFile(dest);
		String line="";
		while((line=reader.readLine())!=null){
			String[] ids=line.split(uidFriIdsSeperater);
			for(String id:ids){
				writer.write(id);
				List<String> tags=uidTags.get(id);
				if(tags!=null){
					for(String tag:tags){
						writer.write(destSeperater+tag);
					}
				}
				writer.write("\r\n");
			}
		}
		writer.close();
	}
	//same line num uidTag and uidFris must have same uid
	public static void generateBalanceUserSet(String uidTags,String uidTagsSeperater,
			String uidFris,String uidFrisSeperater,Map<String,Integer> allIdCateMap,int cateSize) throws Exception{

		Map<Integer,List<String>> cateIdsMap=MyToolKit.getCateIdsMap(uidTags, uidTagsSeperater, allIdCateMap);
		Set<String> balanceCateIds=MyToolKit.generateCateBalanceIdSet(cateIdsMap, cateSize);
		int everyCateUserNum=balanceCateIds.size()/cateIdsMap.size();

		String destUidTags=FileTool.getParentPath(uidTags)+"\\"+FileTool.getPureFileName(uidTags)+"_balanceCate_"+
				cateIdsMap.size()+"x"+everyCateUserNum+".txt";
		String destUidFris=FileTool.getParentPath(uidFris)+"\\"+FileTool.getPureFileName(uidFris)+"_balanceCate_"+
				cateIdsMap.size()+"x"+everyCateUserNum+".txt";
		PrintWriter writerDestUidTags=FileTool.getPrintWriterForFile(destUidTags);
		PrintWriter writerDestUidFris=FileTool.getPrintWriterForFile(destUidFris);

		BufferedReader readerUidTags=FileTool.getBufferedReaderFromFile(uidTags);
		BufferedReader readerUidFris=FileTool.getBufferedReaderFromFile(uidFris);
		String lineUidTags="";
		String lineUidFris="";
		while((lineUidTags=readerUidTags.readLine())!=null&&(lineUidFris=readerUidFris.readLine())!=null){
			String[] elmsUidTags=lineUidTags.split(uidTagsSeperater,2);
			String[] elmsUidFris=lineUidFris.split(uidFrisSeperater, 2);
			String uid=elmsUidTags[0].trim();
			String tags=elmsUidTags[1];
			String fris=elmsUidFris[1];

			if(!uid.equals(elmsUidFris[0].trim())){
				throw new Exception("lineUidTags uid="+elmsUidTags[0]+",but elmsUidTags uid="+elmsUidFris[0]+", two uid not same!");
			}
			if(balanceCateIds.contains(uid)){
				writerDestUidTags.write(uid+uidTagsSeperater+tags+"\r\n");
				writerDestUidFris.write(uid+uidFrisSeperater+fris+"\r\n");
			}
		}
		writerDestUidTags.close();
		writerDestUidFris.close();

	}

	public static void generateBalanceUserSet(String uidTags,String uidTagsSeperater,
			String uidFris,String uidFrisSeperater,String allIdCateFile,String allIdCateSeperater,int cateSize) throws Exception{
		Map<String,Integer> allIdCateMap=IO.readIdCateMapFromFile(allIdCateFile, allIdCateSeperater);
		generateBalanceUserSet(uidTags,uidTagsSeperater,uidFris,uidFrisSeperater,allIdCateMap,cateSize);
	}

	//
	public static void generateIdAndFrisIdsForIdW2v(String idFrids,String seperater,String dest) throws IOException
	{

		BufferedReader reader=FileTool.getBufferedReaderFromFile(idFrids);
		PrintWriter writer=FileTool.getPrintWriterForFile(dest);

		String line="";
		int min=Integer.MAX_VALUE;
		int max=Integer.MIN_VALUE;
		int count=0;
		int lineNum=0;
		int uidRepeatNum=3;
		while((line=reader.readLine())!=null)
		{
			++lineNum;
			if(line.equals(""))
			{
				System.out.println("line "+lineNum+" of idFrids no content");
				continue;
			}
			String[] elms=line.split(seperater);
			String uid=elms[0].trim();

			int friNum=elms.length-1;
			count+=(friNum+uidRepeatNum);
			if(min>friNum+uidRepeatNum)
			{
				min=friNum+uidRepeatNum;
			}
			if(max<friNum+uidRepeatNum)
			{
				max=friNum+uidRepeatNum;
			}

			int i=0;
			int k=0;
			while(i<friNum+uidRepeatNum)
			{
				if(i==friNum/2)
				{
					writer.write(seperater+uid);
					k=i-1;
				}
				else
				{
					if(k==0)
					{
						writer.write(uid);
					}
					else if(k>friNum)
					{
						writer.write(seperater+uid);
					}
					else
					{
						writer.write(seperater+elms[k]);
					}
				}
				++k;
				++i;
			}
			writer.write("\r\n");	
		}
		System.out.println("min id num:"+min+"\t max id num:"+max+"\t avg id num:"+(count*1.0)/lineNum);
		writer.close();
	}

	//special for w2v
	public static void extractUidAndFriIds(String idFrisReview,String itemSeperater,String inerSeperater,String dest) throws IOException
	{
		BufferedReader reader=FileTool.getBufferedReaderFromFile(idFrisReview);
		PrintWriter writer=FileTool.getPrintWriterForFile(dest);

		String line="";
		int min=Integer.MAX_VALUE;
		int max=Integer.MIN_VALUE;
		int count=0;
		int lineNum=0;
		while((line=reader.readLine())!=null)
		{
			++lineNum;
			String[] elms=line.split(itemSeperater);
			String uid=elms[0].trim();
			String frisStr=elms[3];
			String[] fris=frisStr.split(inerSeperater);

			int friNum=fris.length;
			count+=(friNum+1);
			if(min>friNum)
			{
				min=friNum+1;
			}
			if(max<friNum)
			{
				max=friNum+1;
			}

			int i=0;
			int k=0;
			while(i<friNum+1)
			{
				if(i==friNum/2)
				{
					writer.write(inerSeperater+uid);
					k=i-1;
				}
				else
				{
					if(k==0)
					{
						writer.write(fris[k]);
					}
					else
					{
						writer.write(inerSeperater+fris[k]);
					}
				}
				++k;
				++i;
			}
			writer.write("\r\n");	
		}
		System.out.println("min id num:"+min+"\t max id num:"+max+"\t avg id num:"+(count*1.0)/lineNum);
		writer.close();
	}


	public static void extractUidVecForSvm(String uids,String uidSeperater,String idAvgTagsVec,String idVecSeperater,String destFeature,String destSeperater) throws IOException
	{
		Map<String,String> idVecMap=IO.readWordVecMap(idAvgTagsVec, idVecSeperater);
		BufferedReader reader=FileTool.getBufferedReaderFromFile(uids);
		PrintWriter writer=FileTool.getPrintWriterForFile(destFeature);
		String line="";
		while((line=reader.readLine())!=null)
		{
			String[] elms=line.split(uidSeperater,2);
			String uid=elms[0].trim();
			String vec=idVecMap.get(uid);
			String diemVec=vec2DiemVec(vec,idVecSeperater,destSeperater);
			writer.write(uid+destSeperater+diemVec+"\r\n");
		}

		writer.close();
	}

	public static void extractUidAvgItemVecForSvm(String uids,String uidSeperater,String innerSeperater,String keyVec,String keyVecSeperater,String destFeature,String destSeperater) throws IOException
	{
		Map<String,List<Double>> idVecMap=IO.readKeyVecMap(keyVec, keyVecSeperater,1);
		BufferedReader reader=FileTool.getBufferedReaderFromFile(uids);
		PrintWriter writer=FileTool.getPrintWriterForFile(destFeature);
		String line="";
		while((line=reader.readLine())!=null)
		{
			String[] elms=line.split(uidSeperater);
			String uid=elms[0].trim();
			//System.out.println(uid);
			String friStr=elms[3];
			String[] fris=friStr.split(innerSeperater);
			List<Double> avgVec=MyToolKit.cptItemsAvgVec(fris, idVecMap);
			String diemVec=vec2DiemVec(avgVec,destSeperater);
			writer.write(uid+destSeperater+diemVec+"\r\n");
		}

		writer.close();
	}

	protected static String vec2DiemVec(String vec,String vecSeperater,String diemVecSeperater)
	{
		StringBuilder stb=new StringBuilder();
		if(vec==null||vec.equals(""))
		{
			stb.append("1:0");
		}
		else
		{
			String[] elms=vec.split(vecSeperater);
			for(int i=0;i<elms.length;++i)
			{
				if(i==elms.length-1)
				{
					stb.append((i+1)+":"+elms[i]);

				}
				else
				{
					stb.append((i+1)+":"+elms[i]+diemVecSeperater);
				}
			}
		}
		return stb.toString();
	}

	protected static String vec2DiemVec(List<Double> vec,String diemVecSeperater)
	{
		StringBuilder stb=new StringBuilder();
		if(vec==null||vec.size()==0)
		{
			stb.append("1:0");
		}
		else
		{
			for(int i=0;i<vec.size();++i)
			{
				if(i==vec.size()-1)
				{
					stb.append((i+1)+":"+vec.get(i));

				}
				else
				{
					stb.append((i+1)+":"+vec.get(i)+diemVecSeperater);
				}
			}
		}
		return stb.toString();

	}

	public static void generateUidFidMultiLine(String uidFidLine,String seperater1,String destUidFidMultiLine,String seperater2) throws IOException
	{
		BufferedReader reader=FileTool.getBufferedReaderFromFile(uidFidLine);
		PrintWriter writer=FileTool.getPrintWriterForFile(destUidFidMultiLine);
		Set<String> idSet=new LinkedHashSet<String>();

		String line="";
		while((line=reader.readLine())!=null)
		{
			if(line.equals(""))
			{
				continue;
			}
			String[] elms=line.split(seperater1);
			for(String e:elms)
			{
				idSet.add(e.trim());
			}

		}
		System.out.println("idSet size is "+idSet.size());
		IO.writeSet(idSet, writer);
		writer.close();
	}

	public static void generateClusterFri(String idFirsReview,String itemSeperater,String inerSeperater,String idVec, String idVecSeperater,
			String destIdClusterFriReview,String destClusterFriVec,int clusterStep ) throws IOException
	{
		Map<String,List<Double>> idVecMap=IO.readKeyVecMap(idVec, " ",1);
		int embedingSize=IO.readVecDiem(idVec, "\\s{1,}");
		BufferedReader reader=FileTool.getBufferedReaderFromFile(idFirsReview);
		PrintWriter clusterFriRvWriter=FileTool.getPrintWriterForFile(destIdClusterFriReview);
		PrintWriter clusterFriVec=FileTool.getPrintWriterForFile(destClusterFriVec);

		String line="";
		int lineNum = 0;
		float rndBase = 0.001f;
		while((line=reader.readLine())!=null)
		{
			if (lineNum == 0) {
				clusterFriVec.write("X " + embedingSize + "\r\n");
				clusterFriVec.write("</s>" + idVecSeperater);
				Random rnd = new Random();
				String rndVec = MyToolKit.generateRandomVec(rnd, -1 * rndBase, 1 * rndBase, embedingSize,
						idVecSeperater);
				clusterFriVec.write(rndVec);
				clusterFriVec.write("\r\n");
			}

			String[] elms=line.split(itemSeperater);
			String uid=elms[0];
			String src=elms[1];
			String cate=elms[2];
			String frisStr=elms[3];

			clusterFriRvWriter.write(uid+itemSeperater+src+itemSeperater+cate+itemSeperater);

			clusterFri(uid,frisStr,inerSeperater,clusterStep,idVecMap,clusterFriRvWriter,clusterFriVec,idVecSeperater);
			++lineNum;
		}

		clusterFriRvWriter.close();
		clusterFriVec.close();
	}

	protected static void clusterFri(String uid,String frisStr,String friSeperater,int clusterStep,Map<String,List<Double>> idVecMap,PrintWriter clusterFriRvWriter,PrintWriter clusterFVWriter,String fvSeperater)
	{
		if(frisStr!=null&&!frisStr.equals(""))
		{
			String[] elms=frisStr.split(friSeperater);
			List<List<String>> folds=getFoldElms(elms,clusterStep);

			for(int i=0;i<folds.size();++i)
			{
				List<String> fold=folds.get(i);
				String clusterId=uid+"_"+fold.get(0)+"_"+fold.size();

				if(i==folds.size()-1)
				{
					clusterFriRvWriter.write(clusterId);
				}
				else
				{
					clusterFriRvWriter.write(clusterId+friSeperater);
				}
				List<Double> avgvec=MyToolKit.cptItemsAvgVec(fold, idVecMap);
				clusterFVWriter.write(clusterId);
				for(Double d:avgvec)
				{
					clusterFVWriter.write(fvSeperater+d);
				}
				clusterFVWriter.write("\r\n");
			}
		}
		clusterFriRvWriter.write("\r\n");
		clusterFriRvWriter.flush();
		clusterFVWriter.flush();
	}

	protected static List<List<String>> getFoldElms(String [] elms,int step)
	{
		List<List<String>> folds=new ArrayList<List<String>>();
		if(elms!=null)
		{
			if(elms.length<step)
			{
				for(int i=0;i<elms.length;++i)
				{
					List<String> fold=new ArrayList<String>();
					fold.add(elms[i]);
					folds.add(fold);
				}
			}
			else
			{
				int foldNum=(int) Math.ceil(1.0*elms.length/(1.0*step));
				for(int i=0;i<foldNum;++i)
				{
					List<String> fold=new ArrayList<String>();
					if(i==foldNum-1)
					{
						for(int k=step*i;k<elms.length;++k)
						{
							fold.add(elms[k]);
						}
					}
					else
					{
						for(int k=step*i;k<step*(i+1);++k)
						{
							fold.add(elms[k]);
						}
					}
					folds.add(fold);
				}
			}
		}
		return folds;
	}

	public static void extractIdFrisReviewByTopn(String idFrisReview,String itemSeperater,String innerSeperater,String idVec,String idVecSeperater,
			String destidSimFrisReview,int topn) throws IOException
	{
		Map<String,double[]> idVecMap=IO.readKeyVecMapV1(idVec, idVecSeperater, 1);
		BufferedReader reader=FileTool.getBufferedReaderFromFile(idFrisReview);
		PrintWriter writer=FileTool.getPrintWriterForFile(destidSimFrisReview);
		String line="";
		while((line=reader.readLine())!=null)
		{
			String[] elms=line.split(itemSeperater);
			String id=elms[0];
			String src=elms[1];
			String cate=elms[2];
			writer.write(id+itemSeperater+src+itemSeperater+cate+itemSeperater);
			String frisStr="";
			if(elms.length>3)
			{
				frisStr=elms[3];
			}
			String[] fris=frisStr.split(innerSeperater);
			List<Item> items=cmptItemsScore(id,fris,idVecMap);
			List<Item> selItems=selItemsByTopNScore(items,topn);
			int i=0;
			for(Item it:selItems)
			{
				if(i==selItems.size()-1)
				{
					writer.write(it.word);
				}
				else
				{
					writer.write(it.word+innerSeperater);
				}
				++i;
			}
			writer.write("\r\n");
		}

		writer.close();

	}
	public static void extractIdFrisReviewBySimThrehold(String idFrisReview,String itemSeperater,String innerSeperater,String idVec,String idVecSeperater,
			String destidSimFrisReview,double simThreshold) throws IOException
	{

		Map<String,double[]> idVecMap=IO.readKeyVecMapV1(idVec, idVecSeperater, 1);
		BufferedReader reader=FileTool.getBufferedReaderFromFile(idFrisReview);
		PrintWriter writer=FileTool.getPrintWriterForFile(destidSimFrisReview);
		String line="";
		while((line=reader.readLine())!=null)
		{
			String[] elms=line.split(itemSeperater);
			String id=elms[0];
			String src=elms[1];
			String cate=elms[2];
			writer.write(id+itemSeperater+src+itemSeperater+cate+itemSeperater);
			String frisStr="";
			if(elms.length>3)
			{
				frisStr=elms[3];
			}
			String[] fris=frisStr.split(innerSeperater);
			List<Item> items=cmptItemsScore(id,fris,idVecMap);
			List<Item> selItems=selItemByThrsholdScore(items,simThreshold);
			int i=0;
			for(Item it:selItems)
			{
				if(i==selItems.size()-1)
				{
					writer.write(it.word);
				}
				else
				{
					writer.write(it.word+innerSeperater);
				}
				++i;
			}
			writer.write("\r\n");
		}

		writer.close();
	}

	public static List<Item> cmptItemsScore(String my,String[] its,Map<String,double[]> keyVecMap)
	{
		List<String> itList=new ArrayList<String>();
		for(String i:its)
		{
			itList.add(i);
		}
		return cmptItemsScore(my,itList,keyVecMap);
	}

	public static List<Item> cmptItemsScore(String my,List<String> itList,Map<String,double[]> keyVecMap)
	{
		List<Item> items=new ArrayList<Item>();
		double[] myVec =keyVecMap.get(my);
		for(String i:itList)
		{
			double[] ivec=keyVecMap.get(i);
			double cosSim=0;
			if(ivec!=null&&myVec!=null)
			{
				cosSim=MyMathTool.computeCosSim(myVec, ivec);
			}
			Item item=new Item(i);
			item.score=cosSim;
			items.add(item);
		}
		return items;
	}

	public static void sortItemsByDscScore(List<Item> items)
	{
		Collections.sort(items, new Comparator<Item>(){

			@Override
			public int compare(Item o1, Item o2) {
				// TODO Auto-generated method stub
				return o2.score.compareTo(o1.score);
			}});
	}

	public static List<Item> selItemsByTopNScore(List<Item> items,int topN)
	{
		sortItemsByDscScore(items);
		List<Item> tpnItems=new ArrayList<Item>();
		int tpn=topN<items.size()?topN:items.size();
		for(int i=0;i<tpn;++i)
		{
			tpnItems.add(items.get(i));
		}
		return tpnItems;
	}

	public static List<Item> selItemByThrsholdScore(List<Item> items,double thrScore)
	{
		//sortItemsByDscScore(items);
		List<Item> seltems=new ArrayList<Item>();
		for(Item item:items)
		{
			if(item.score>=thrScore)
			{
				seltems.add(item);
			}
		}
		sortItemsByDscScore(seltems);
		return seltems;
	}

	protected static List<String> getTagsFromJsonLine(String jsonLine){
		JSONObject jobj=JSONObject.fromObject(jsonLine);
		return JSONObject.fromObject(jobj.get("tags").toString()).getJSONArray("tags");
	}
	protected static List<String> getFriIdsFromJsonLine(String jsonLine){
		JSONObject jobj=JSONObject.fromObject(jsonLine);
		return jobj.getJSONArray("uids");
	}
	protected static String getJsonLineValueOf(String key,String jsonLine){
		JSONObject jobj=JSONObject.fromObject(jsonLine);
		return jobj.get(key).toString();
	}

	//	public static void main(String[] args)
	//	{
	//		String[] elms={"1","2","3"/*,"4","5","6","7","8"*/};
	//		List<List<String>> ll=getFoldElms(elms,3);
	//		System.out.println("helllo");
	//	}

}
