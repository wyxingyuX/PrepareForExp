package func;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import Jama.Matrix;
import utils.FileTool;
import utils.IO;
import utils.MyMathTool;
import utils.MyToolKit;
import utils.TestTool;

public class StackVec {
	Map<String, double[]> wordsVecMap;
	Map<String, double[]> uidVecMap;
	int vecLength;
	public final static String TAG = "tag";
	public final static String UID = "uid";

	public void loadVecMap(String keyVecfilePath, int vecLength, String seperater, String type) throws Exception {
		switch (type) {
		case TAG:
			this.wordsVecMap = this.loadKeyVecMatrixFromFile(keyVecfilePath, vecLength, seperater);
			break;
		case UID:
			this.uidVecMap = this.loadKeyVecMatrixFromFile(keyVecfilePath, vecLength, seperater);
			break;
		}
	}

	protected Map<String, double[]> loadKeyVecMatrixFromFile(String keyVecfilePath, int vecLength, String seperater)
			throws Exception {
		File keyVecfile = new File(keyVecfilePath);
		return this.loadKeyVecMatrixFromFile(keyVecfile, vecLength, seperater);
	}

	protected Map<String, double[]> loadKeyVecMatrixFromFile(File keyVecfile, int vecLength, String seperater)
			throws Exception {
		if (!keyVecfile.exists()) {
			TestTool.println("warn:" + keyVecfile.getAbsolutePath() + " don't exists.");
			return null;
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(keyVecfile), "UTF-8"));
		Map<String, double[]> myKeyVecMatrix = new LinkedHashMap<String, double[]>();

		this.vecLength = vecLength;
		String curLine = "";
		String[] elms = null;
		String itemSeperater = ":";
		br.readLine();// ignore 1st line
		int lineNum=1;
		while ((curLine = br.readLine()) != null) {
			elms = curLine.split(seperater);
			double[] vec = new double[vecLength];

			if (1 != elms.length) {
				if (elms.length != vecLength + 1) {
					System.out.println(keyVecfile.getAbsolutePath()+"\r\n"+lineNum+"line。"+curLine);
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
			++lineNum;
		}
		br.close();
		return myKeyVecMatrix;
	}

	public Matrix getStackVecMatrix(String[] keys, String type) {
		Matrix stackMatrix = new Matrix(keys.length, vecLength);
		Map<String, double[]> vecs = null;
		switch (type) {
		case TAG:
			vecs = this.wordsVecMap;
			break;
		case UID:
			vecs = this.uidVecMap;
			break;
		default:
			vecs = this.wordsVecMap;
		}

		for (int i = 0; i < keys.length; ++i) {
			String key = keys[i];
			double[] vec = vecs.get(key);
			if(vec==null)
			{
				vec=new double[vecLength];
			}
			for (int j = 0; j < vec.length; ++j) {
				stackMatrix.set(i, j, vec[j]);
			}
		}
		return stackMatrix;
	}

	public void computeAndWriteEmbedingInUserSpace(String uid, String[] words, Matrix wordsStackMatrix,
			Matrix friIdsStatckMatrix, int maxDiems, PrintWriter writer, String destSeperater)
					throws UnsupportedEncodingException, FileNotFoundException {
		Matrix embedMaInUserSpace = wordsStackMatrix.times(friIdsStatckMatrix.transpose());
		for (int i = 0; i < words.length; ++i) {
			writer.write(uid + "_" + words[i]);

			for (int j = 0; j < maxDiems; ++j) {
				if (j < embedMaInUserSpace.getColumnDimension()) {
					writer.write(" " + embedMaInUserSpace.get(i, j));
				} else {
					writer.write(" " + 0);
				}
			}
			writer.write("\r\n");
		}
		writer.flush();
	}

	public void computeAndWriteEmbedingInUserSpaceFromFile(String uidTagsReview, String uidFriIdsReview,
			String sourceSeperater, String destEmbeding, String destEmbedingSeperater, int embedingSize,
			String destUidSpecficTagsReview) throws IOException {

		BufferedReader readerTags = FileTool.getBufferedReaderFromFile(uidTagsReview);
		BufferedReader readerFriIds = FileTool.getBufferedReaderFromFile(uidFriIdsReview);
		PrintWriter embedingWriter = FileTool.getPrintWriterForFile(destEmbeding, true);
		PrintWriter uisSpecficTagsWriter = FileTool.getPrintWriterForFile(destUidSpecficTagsReview);

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

			if (elmsTags.length < 4 || elmsFriIds.length < 4) {
				System.out.println(uid + " elmsTags.length=" + elmsTags.length + ", elmsFriIds.length="
						+ elmsFriIds.length + ", now use random vec");

				// Random generate user who have tag but no friends , tag
				// embeding in user space =random vec
				if (elmsTags.length >= 4) {
					String tags = elmsTags[3];
					String[] words = tags.split("\t");

					// generate user specific tag
					StringBuilder stb = new StringBuilder();
					for (int k = 0; k < words.length; ++k) {
						if (k == words.length - 1) {
							stb.append(uid + "_" + words[k]);
						} else {
							stb.append(uid + "_" + words[k] + "\t");
						}
					}
					uisSpecficTagsWriter.write(uid + sourceSeperater + source + sourceSeperater + cate + sourceSeperater
							+ stb.toString() + "\r\n");

					// generater random vec
					for (String tag : words) {
						embedingWriter.write(uid + "_" + tag + destEmbedingSeperater);
						Random rnd = new Random();
						String rndVec = MyToolKit.generateRandomVec(rnd, -1 * rndBase, 1 * rndBase, embedingSize,
								destEmbedingSeperater);
						embedingWriter.write(rndVec);
						embedingWriter.write("\r\n");
					}
				}

			} else {
				String tags = elmsTags[3];
				String friIds = elmsFriIds[3];

				String[] fids = friIds.split("\t");
				String[] words = tags.split("\t");

				// generate user specific tag
				StringBuilder stb = new StringBuilder();
				for (int k = 0; k < words.length; ++k) {
					if (k == words.length - 1) {
						stb.append(uid + "_" + words[k]);
					} else {
						stb.append(uid + "_" + words[k] + "\t");
					}
				}
				uisSpecficTagsWriter.write(uid + sourceSeperater + source + sourceSeperater + cate + sourceSeperater
						+ stb.toString() + "\r\n");

				// generate tag embedding in user space
				Matrix tagsStackMatrix = this.getStackVecMatrix(words, this.TAG);
				Matrix fidsStackMatrix = this.getStackVecMatrix(fids, this.UID);
				this.computeAndWriteEmbedingInUserSpace(uid, words, tagsStackMatrix, fidsStackMatrix, embedingSize,
						embedingWriter, " ");
			}
			++lineNum;
		}
		embedingWriter.close();
		uisSpecficTagsWriter.close();
	}

	protected double[] computeFrisAvgVec(String[] fris)
	{
		double[] avgVec=null;
		int count=0;
		for(String fri:fris)
		{
			double[] vec=this.uidVecMap.get(fri);
			if(vec==null)
			{
				System.out.println("fri "+fri+" no vec");
				continue;
			}
			if(avgVec==null)
			{
				avgVec=new double[vec.length];
			}

			for(int i=0;i<avgVec.length;++i)
			{
				avgVec[i]+=vec[i];
			}
			++count;
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

	protected double[] computeSimWeightFrisAvgVec(String uid,String[] fris)
	{
		double[] myVec=this.uidVecMap.get(uid);
		if(myVec==null)
		{
			System.out.println("error: "+uid+" no vec.");
			return null;
		}
		double[] simWeightAvgVec=null;
		for(String fri:fris)
		{
			double[] friVec=this.uidVecMap.get(fri);
			if(friVec==null)
			{
				System.out.println("fri "+fri+" no vec");
				continue;
			}
			if(simWeightAvgVec==null)
			{
				simWeightAvgVec=new double[friVec.length];
			}
			double sim=MyMathTool.computeCosSim(myVec, friVec);
			for(int i=0;i<simWeightAvgVec.length;++i)
			{
				simWeightAvgVec[i]+=sim*friVec[i];
			}
		}

		return simWeightAvgVec;
	}
	/*
	 * frisVecAvgType ：0 for normal avg,just divide fri num; 1 for sim weight avg.
	 */
	protected void computeElementMutiAndWriteEmbedingInUserSpace(String uid,String[] tags,String[] fris,PrintWriter writer,String seperater,float times,int frisVecAvgType)
	{

		double[] friAvgVec=null;
		if(fris!=null)
		{
			switch(frisVecAvgType)
			{
			case 0:
				friAvgVec=this.computeFrisAvgVec(fris);
				break;
			case 1:
				friAvgVec=this.computeSimWeightFrisAvgVec(uid, fris);
				break;
			default:
				friAvgVec=this.computeFrisAvgVec(fris);
				break;
			}
		}

		for(String tag:tags)
		{
			double[] tVec=this.wordsVecMap.get(tag);
			double[] elmentMultiVec=null;
			if(friAvgVec!=null&&friAvgVec.length>0)
			{
				elmentMultiVec=MyMathTool.elmentWiseMutliplicationForVec(friAvgVec, tVec);
			}
			else
			{
				elmentMultiVec=tVec;
			}	
			//IO.writeWordVec(uid+"_"+tag, elmentMultiVec, seperater, writer);
			IO.writeWordVecXTimes(uid+"_"+tag, elmentMultiVec, times,seperater, writer);
		}
	}

	public void computeElementMutiAndWriteEmbedingInUserSpaceFromFile(
			String idTagsReview,String idFrisReview,String seperater,String destIdSpecialTagsReview
			,String destEmbeding,String destEmbedingSeperater,float timesOfEmbeding,int frisVecAvgType) throws IOException
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
			String frisStr="";
			if(elmsFrisreview.length>3)
			{
				frisStr=elmsFrisreview[3];
			}
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


			this.computeElementMutiAndWriteEmbedingInUserSpace(uid, tags, fris, embedingWriter, destEmbedingSeperater,timesOfEmbeding,frisVecAvgType);
			++lineNum;
		}

		idSpecialTagsReviewWriter.close();
		embedingWriter.close();
	}

}
