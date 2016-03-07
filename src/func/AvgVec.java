package func;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import Jama.Matrix;
import utils.FileTool;
import utils.IO;
import utils.MyMathTool;
import utils.MyToolKit;
import utils.TestTool;

public class AvgVec {
	Map<String, double[]> wordVecMap;
	int vecLength;
	Map<String,List<String>> uidWordsMap;

	public AvgVec(String uidWordsFile,String uidWordsSeperater,String wordVecFile,String wordVecSeperater) throws Exception {
		// TODO Auto-generated constructor stub
		this.uidWordsMap=this.loadUidWordsMap(uidWordsFile, uidWordsSeperater);
		this.wordVecMap=this.loadKeyVecMatrixFromFile(wordVecFile, wordVecSeperater);
		
	}
	protected Map<String,List<String>> loadUidWordsMap(String uidWordsFile,String seperater) throws IOException
	{
		Map<String,List<String>> myUidWordsMap=new HashMap<String,List<String>>();
		BufferedReader reader=FileTool.getBufferedReaderFromFile(uidWordsFile);
		String line="";
		int lineNum=0;
		while((line=reader.readLine())!=null)
		{
			++lineNum;
			if(line.equals(""))
			{
				System.out.println("Warn: "+uidWordsFile+" line "+lineNum+" no content");
				continue;
			}
			String[] elms=line.split(seperater);
			String uid=elms[0].trim();
			if(elms.length<2)
			{
				System.out.println("Warn: "+uidWordsFile+" line "+lineNum+" have uid"+uid+",but no words");
				continue;
			}
			List<String> wordList=new ArrayList<String>();
			for(int i=1;i<elms.length;++i)
			{
				wordList.add(elms[i]);
			}
			
			myUidWordsMap.put(uid, wordList);
		}
		
		return myUidWordsMap;
		
	}
	protected Map<String, double[]> loadKeyVecMatrixFromFile(String keyVecfilePath, String seperater)
			throws Exception {
		File keyVecfile = new File(keyVecfilePath);
		int vecLength=MyToolKit.getVecDiem(keyVecfilePath, seperater);
		return this.loadKeyVecMatrixFromFile(keyVecfile, vecLength, seperater);
	}

	protected Map<String, double[]> loadKeyVecMatrixFromFile(File keyVecfile, int vecLength, String seperater)
			throws Exception {
		if (!keyVecfile.exists()) {
			TestTool.println("warn:" + keyVecfile.getAbsolutePath() + " don't exists.");
			return null;
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(keyVecfile), "UTF-8"));
		Map<String, double[]> myKeyVecMatrix = new HashMap<String, double[]>();

		this.vecLength = vecLength;
		String curLine = "";
		String[] elms = null;
		br.readLine();// ignore 1st line
		while ((curLine = br.readLine()) != null) {
			elms = curLine.split(seperater);
			double[] vec = new double[vecLength];

			if (1 != elms.length) {
				if (elms.length != vecLength + 1) {
					throw new Exception("elm: " + elms[0] + " vec real length not " + vecLength);
				}
				for (int i = 1; i < elms.length; ++i) {
					double value = Double.parseDouble(elms[i]);
					vec[i - 1] = value;
				}
			} else {
				for (int i = 1; i < vec.length; ++i) {
					vec[i - 1] = 0;
				}
			}
			myKeyVecMatrix.put(elms[0], vec);
		}
		br.close();
		return myKeyVecMatrix;
	}

	protected double[] computeWordsAvgVec(List<String> tags)
	{
		double[] avgVec=null;
		int count=0;
		for(String tag:tags)
		{
			double[] vec=this.wordVecMap.get(tag);
			if(vec!=null)
			{
				++count;
				if(avgVec==null)
				{
					avgVec=new double[vec.length];
				}
				for(int i=0;i<avgVec.length;++i)
				{
					avgVec[i]+=vec[i];
				}
			}
			else
			{
				System.out.println("warn:"+tag+" no vec");
			}
		}
		if(avgVec!=null)
		{
			for(int i=0;i<avgVec.length;++i)
			{
				avgVec[i]/=count;
			}
		}
		return avgVec;
	}
	private double[] computeWordsSumVec(List<String> tags)
	{
		double[] sumVec=null;
		int count=0;
		for(String tag:tags)
		{
			double[] vec=this.wordVecMap.get(tag);
			if(vec!=null)
			{
				++count;
				if(sumVec==null)
				{
					sumVec=new double[vec.length];
				}
				for(int i=0;i<sumVec.length;++i)
				{
					sumVec[i]+=vec[i];
				}
			}
			else
			{
				System.out.println("warn:"+tag+" no vec");
			}
		}

		return sumVec;
	}

	//divide friNum which have vec
	protected double[] computeFriWordsAvgVec(List<String> friIds)
	{
		double[] avgVec=null;
		int count=0;
		for(String friId:friIds)
		{
			List<String> words=this.uidWordsMap.get(friId);
			if(words==null)
			{
				System.out.println("warn:"+friId+" no words");
				continue;
			}

			double[] wordsVec=this.computeWordsSumVec(words);
			if(wordsVec==null)
			{
				continue;
			}
			++count;

			if(avgVec==null)
			{
				avgVec=new double[wordsVec.length];
			}

			for(int i=0;i<avgVec.length;++i)
			{
				avgVec[i]+=wordsVec[i];
			}

		}

		if(avgVec!=null)
		{
			for(int i=0;i<avgVec.length;++i)
			{
				avgVec[i]/=count;
			}
		}

		return avgVec;
	}

	private Matrix computeTAvgXFTAvg(List<String> tags,List<String> friIds)
	{
		double[] tAvgVec=this.computeWordsAvgVec(tags);
		double[] FTAvgVec=this.computeFriWordsAvgVec(friIds);
		Matrix tAvgMat=MyMathTool.vec2Matrix(tAvgVec);//one row
		Matrix ftAvgMat=MyMathTool.vec2Matrix(FTAvgVec);//one row
		return tAvgMat.transpose().times(ftAvgMat);
	}
    private void computeAndWriteTAvgXFTAvg(String uid,List<String> tags,List<String> friIds,PrintWriter writer,String seperater)
    {
    	Matrix resultMatrix=this.computeTAvgXFTAvg(tags, friIds);
    	for(int i=0;i<resultMatrix.getRowDimension();++i)
    	{
    		String specficDiemName=uid+"_"+(i+1);
    		writer.write(specficDiemName);
    		for(int j=0;j<resultMatrix.getColumnDimension();++j)
    		{
    			writer.write(seperater+resultMatrix.get(i, j));
    		}
    		writer.write("\r\n");
    	}
    	writer.flush();
    }
    private void computeAndWriteTAvgXFTAvg(String uid,String[] tags,String[] friIds,PrintWriter writer,String seperater)
    {
    	List<String> tagList=new ArrayList<String>();
    	List<String> friIdList=new ArrayList<String>();
    	for(String tag:tags)
    	{
    		tagList.add(tag);
    	}
    	for(String friId:friIds)
    	{
    		friIdList.add(friId);
    	}
    	
    	this.computeAndWriteTAvgXFTAvg(uid, tagList, friIdList, writer, seperater);
    }
    
	public void computeAndWriteDiemEmbeingInAvgUserSpace(String uidTagsReview, String uidFriIdsReview,
			String sourceSeperater, String destEmbeding, String destEmbedingSeperater, int embedingSize,
			String destUidSpecficTagsReview) throws IOException
	{
		BufferedReader readerTags = FileTool.getBufferedReaderFromFile(uidTagsReview);
		BufferedReader readerFriIds = FileTool.getBufferedReaderFromFile(uidFriIdsReview);
		PrintWriter embedingWriter = FileTool.getPrintWriterForFile(destEmbeding, true);
		PrintWriter uisSpecficDiemEmbedingWriter = FileTool.getPrintWriterForFile(destUidSpecficTagsReview);

		String lineTags = "";
		String lineFriIds = "";
		int lineNum = 0;
		float rndBase = 0.001f;
		while ((lineTags = readerTags.readLine()) != null && (lineFriIds = readerFriIds.readLine()) != null) {
			if (lineNum == 0) {
				embedingWriter.write("X " + embedingSize + "\r\n");
				embedingWriter.write("</s>" + destEmbedingSeperater);
				Random rnd = new Random();
				String rndVec = MyToolKit.generateRandomVec(rnd, -1 * rndBase, 1 * rndBase, embedingSize,
						destEmbedingSeperater);
				embedingWriter.write(rndVec);
				embedingWriter.write("\r\n");
			}

			String[] elmsTags = lineTags.split(sourceSeperater);
			String uid = elmsTags[0];
			String source = elmsTags[1];
			String cate = elmsTags[2];

			String[] elmsFriIds = lineFriIds.split(sourceSeperater);


			String tags = elmsTags[3];
			String friIds = elmsFriIds[3];

			String[] fids = friIds.split("\t");
			String[] words = tags.split("\t");

			// generate user specific word embedding diem
			StringBuilder stb = new StringBuilder();
			for (int k = 0; k < embedingSize; ++k) {
				if (k == words.length - 1) {
					stb.append(uid + "_" + (k+1));
				} else {
					stb.append(uid + "_" + (k+1) + "\t");
				}
			}
			uisSpecficDiemEmbedingWriter.write(uid + sourceSeperater + source + sourceSeperater + cate + sourceSeperater
					+ stb.toString() + "\r\n");

			// generate tag embedding in user space
			this.computeAndWriteTAvgXFTAvg(uid, words, fids, embedingWriter, " ");
			++lineNum;
		}
		embedingWriter.close();
		uisSpecficDiemEmbedingWriter.close();

	}

	protected void computeElementMutiAndWriteEmbedingInUserSpace(String uid,String[] tags,String[] fris,PrintWriter writer,String seperater,float times)
	{
        List<String> friIds=new ArrayList<String>();
        for(String fri:fris)
        {
        	friIds.add(fri);
        }
		double[] friWordsAvgVec=this.computeFriWordsAvgVec(friIds);
		
		for(String tag:tags)
		{
			double[] tVec=this.wordVecMap.get(tag);
			double[] elmentMultiVec=MyMathTool.elmentWiseMutliplicationForVec(friWordsAvgVec, tVec);
			//IO.writeWordVec(uid+"_"+tag, elmentMultiVec, seperater, writer);
			IO.writeWordVecXTimes(uid+"_"+tag, elmentMultiVec, times,seperater, writer);
		}
	}

	public void computeElementMutiAndWriteEmbedingInUserSpaceFromFile(
			String idTagsReview,String idFrisReview,String seperater,String destIdSpecialTagsReview
			,String destEmbeding,String destEmbedingSeperater,float timesOfEmbeding) throws IOException
	{
		BufferedReader idTagsReviewReader=FileTool.getBufferedReaderFromFile(idTagsReview);
		BufferedReader idFrisReviewReader=FileTool.getBufferedReaderFromFile(idFrisReview);
		PrintWriter embedingWriter=FileTool.getPrintWriterForFile(destEmbeding);
		PrintWriter idSpecialTagsReviewWriter=FileTool.getPrintWriterForFile(destIdSpecialTagsReview);

		String idTagsReviewLine="";
		String idFriReviewLine="";

		String itemSeperater=seperater;
		String itemInnerSeperater="\t";
		int lineNum=0;
		float rndBase = 0.001f;
		int embedingSize=this.vecLength;
		while((idTagsReviewLine=idTagsReviewReader.readLine())!=null&&(idFriReviewLine=idFrisReviewReader.readLine())!=null)
		{
			if (lineNum == 0)
			{
				embedingWriter.write("X " + embedingSize + "\r\n");
				embedingWriter.write("</s>" + destEmbedingSeperater);
				Random rnd = new Random();
				String rndVec = MyToolKit.generateRandomVec(rnd, -1 * rndBase, 1 * rndBase, embedingSize,destEmbedingSeperater);
				embedingWriter.write(rndVec);
				embedingWriter.write("\r\n");
			}

			String[] elmsTagsReview=idTagsReviewLine.split(itemSeperater);
			String uid=elmsTagsReview[0];
			String src=elmsTagsReview[1];
			String cate=elmsTagsReview[2];
			String tagsStr=elmsTagsReview[3];
			String[] tags=tagsStr.split(itemInnerSeperater);

			String[] elmsFrisreview=idFriReviewLine.split(itemSeperater);
			String frisStr=elmsFrisreview[3];
			String[] fris=frisStr.split(itemInnerSeperater);

			idSpecialTagsReviewWriter.write(uid+itemSeperater+src+itemSeperater+cate+itemSeperater);
			StringBuilder stb=new StringBuilder();
			for(int i=0;i<tags.length;++i)
			{
				if(i==tags.length-1)
				{
					stb.append(uid+"_"+tags[i]);
				}
				else
				{
					stb.append(uid+"_"+tags[i]+itemInnerSeperater);	
				}

			}

			idSpecialTagsReviewWriter.write(stb.toString()+"\r\n");


			this.computeElementMutiAndWriteEmbedingInUserSpace(uid, tags, fris, embedingWriter, destEmbedingSeperater,timesOfEmbeding);
			++lineNum;
		}

		idSpecialTagsReviewWriter.close();
		embedingWriter.close();
	}


}
