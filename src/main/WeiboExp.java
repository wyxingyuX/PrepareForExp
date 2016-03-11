package main;

import java.io.IOException;

import func.ExtractData;
import func.Func;
import func.StackVec;
import func.UserVec;
import func.WeiboVec;
import func.WordSetVec;
import utils.FileTool;
import utils.MyToolKit;

public class WeiboExp {  
	public static void mergeSubDirWeibos2OneDirWeibos() throws Exception
	{
		String base="F:\\ExpData\\DataFromOther\\crawl\\";
		MyToolKit.mergeFilesForFileFoldDir(base, "Weibos\\", base+"all\\Weibos\\");
	}
	public static void excExtractWeibo() throws IOException{ 
		String base="F:\\ExpData\\DataFromOther\\crawl\\";
		String jsonDataDir=base+"all\\Weibos\\";
		String weibosFilePath=base+"extractFromAll\\uidWeibos.txt";
		Func.json2Weibos(jsonDataDir, weibosFilePath, "\t");//one uid weibos be write in one line

		//		String uid=base+"extractFromAll\\uid.txt";
		//		String weibo=base+"extractFromAll\\weibos.txt";
		//		Func.split(weibosFilePath, "\t", 1, uid, weibo);
		//		String oneWeiboOneLineFilePath=base+"extarctFromAll\\oneWeiboOneLine.txt";
		//		Func.oneWeiboOneLine(weibo, "\t", oneWeiboOneLineFilePath);
	}
	public static void excuidWeibos2uidWeiboId() throws IOException{
		String base="F:\\ExpData\\DataFromOther\\crawl\\extractFromAll\\";
		String uidWeibos=base+"uidWeibos.txt";
		String uidWeiboIds=base+"uidWeiboIds.txt";
		String weiboIdAndWeibos=base+"weiboIdAndWeibos.txt";
		Func.uidWeibos2uidWeiboId(uidWeibos, "\t", uidWeiboIds, "\t",weiboIdAndWeibos);//auto generate a weiboIdAndWeibos.txt file

		String weiboIds=base+"weiboIds.txt";
		String oneLineWeibos=base+"oneLineWeibos.txt";
		Func.split(weiboIdAndWeibos, "\t", 1, weiboIds, oneLineWeibos);
	}

	public static void excGenerateWeiboVec() throws IOException{
		String base="F:\\ExpData\\DataFromOther\\crawl\\extractFromAll\\";
		String weiboIdAndWeiboFc=base+"uid-400\\weiboIdAndWeibosFc.txt";
		String weiboFcVecFile=base+"all\\oneLineWeibos_fc_vector.txt";

		WeiboVec weiboVec=new WeiboVec();
		weiboVec.loadWordVecMaxtrix(weiboFcVecFile, "\\s{1,}");
		String weiboVecFile=base+"uid-400\\weiboId_avgWord_vector.txt";
		weiboVec.generateWeiboVecAvg(weiboIdAndWeiboFc, "\\s{1,}", weiboVecFile, " ");
	}

	public static void extractSamllUIdWeiboIds() throws IOException
	{
		String base="F:\\ExpData\\DataFromOther\\crawl\\extractFromAll\\";
		String allUidWeiboIds=base+"all\\uidWeiboIds.txt";
		String smallUIds=base+"uid-400\\idCate.txt";
		String destSmallUIdWeiboIds=base+"uid-400\\uidWeiboIds.txt";
		ExtractData.extratcSamllUidItemSet(smallUIds, "\t", allUidWeiboIds, "\t", destSmallUIdWeiboIds);
	}
	public static void extractSamllUIdWeibofc() throws IOException
	{
		String base="F:\\ExpData\\DataFromOther\\crawl\\extractFromAll\\";
		String allUidWeibofc=base+"all\\weiboIdAndWeibosFc.txt";
		String smallUIds=base+"uid-400\\uidWeiboIds.txt";
		String destSmallUIdWeibofc=base+"uid-400\\weiboIdAndWeibosFc.txt";
		ExtractData.extractSmallIdsUidItemsSet(smallUIds, "\t", allUidWeibofc, "\\s{1,}", destSmallUIdWeibofc);
	}

	public static void generateAllUidAvgWeiboVec() throws IOException
	{
		String base="F:\\ExpData\\DataFromOther\\crawl\\extractFromAll\\";
		String uidAndWeiboId=base+"uidWeiboIds.txt";
		String weiboVec=base+"weiboId_avgWord_vector.txt";

		WordSetVec wVec=new WordSetVec();
		wVec.loadWordVecMaxtrix(weiboVec, "\\s{1,}");
		String userIdVec=base+"id_avgW_vector.txt";
		wVec.generateWordsVecAvg(uidAndWeiboId, "\t", userIdVec, " ");

	}

	//	public static void excGenerateUserVec() throws IOException{
	//		String base="F:\\ExpData\\DataIntegate\\source\\nne\\";
	//		String femaleIds=base+"Tags\\female.txt";
	//		String maleIds=base+"Tags\\male.txt";
	//		String wordVecFilePath=base+"Weibos\\weibosFc_vector.txt";
	//		String uidWordFilePath=base+"Weibos\\uidWeibosFc.txt";
	//		String userVecFilePath=base+"Weibos\\userLabelVec_400.txt";
	//
	//		UserVec uvec=new UserVec();
	//		uvec.loadIdsLabel(femaleIds,maleIds);
	//		uvec.loadWordVecMaxtrix(wordVecFilePath, "\\s{1,}");
	//		uvec.generateUserVecAvg(uidWordFilePath, "\\s{1,}", userVecFilePath, "\t");
	//	}
	//	public static void excGenerateUserVecLineForm() throws IOException{
	//		String base="F:\\ExpData\\DataIntegate\\source\\nne\\Weibos\\extractWeibo-10\\avg20weiboLineForm\\";
	//		String wordVecFilePath=base+"weiboId_vector.txt";
	//		String uidWordFilePath=base+"allReview.txt";
	//		String userVecFilePath=base+"weibo(10x20)_avg_feature.txt";
	//
	//		UserVec uvec=new UserVec();
	//		uvec.loadWordVecMaxtrix(wordVecFilePath, "\\s{1,}");
	//		uvec.generateUserVecAvgLineForm(uidWordFilePath, "\t", userVecFilePath, "\t");
	//
	//	}
	//	public static void excGenerateTrainAndTest() throws IOException{
	//		String base="F:\\ExpData\\DataIntegate\\source\\nne\\";
	//		String femaleIds=base+"Tags\\female.txt";
	//		String maleIds=base+"Tags\\male.txt";
	//		String temp="Weibos\\";
	//		String uidLableVecFile=base+temp+"userLabelVec_400.txt";
	//		String trainFile=base+temp+"train\\trainData.txt";
	//		String testFile=base+temp+"test\\testData.txt";
	//
	//		UserVec uvec=new UserVec();
	//		uvec.loadIdsLabel(femaleIds,maleIds);
	//		uvec.generateTrainAndTest(uidLableVecFile, "\t", 0.8f, trainFile, testFile);
	//	}

	//	public static void excGenerate() throws IOException{
	//		UserVec uvec=new UserVec();
	//		String base="F:\\ExpData\\DataIntegate\\source\\nne\\";
	//		String temp="Weibos";
	//		String femaleIds=base+"Tags\\female.txt";
	//		String maleIds=base+"Tags\\male.txt";
	//		uvec.loadIdsLabel(femaleIds,maleIds);
	//		uvec.generateSortIdSourceLabelWordsTrainAndTest(base+temp+"\\uidWeibosFc.txt", "\\s{1,}",0.8f, temp, base+temp+"\\dytText_train.txt", base+temp+"\\dytText_test.txt","\t\t");
	//	}
	public static void generateWeiboReview() throws IOException
	{
		String base="F:\\ExpData\\DataFromOther\\crawl\\extractFromAll\\uid-400\\";
		String idcate=base+"idCate.txt";
		String uidFriIds=base+"uidWeiboIds.txt";
		String dest=base+"idWeiboIdsReview.txt";
		ExtractData.generateUidItemsReviews(uidFriIds, "\t", idcate, "\t", dest);
	}
	public static void excGenerateWeiboReview(int weiboIdsNum) throws IOException{
		String base="F:\\ExpData\\DataIntegate\\source\\nne\\";
		String femaleIds=base+"Tags\\female.txt";
		String maleIds=base+"Tags\\male.txt";
		String uidWeiboIds=base+"Weibos\\extractAllWeibo\\uidWeiboIds.txt";
		String reviewFormFile=base+"Weibos\\allReview.txt";
		Func.uidWeiboId2ReviewForm(femaleIds, maleIds, uidWeiboIds, "\t", reviewFormFile, "\t\t",weiboIdsNum);
	}

	public static void excGenerateAvgXWeibosReview(int avgNum) throws IOException{
		String base="F:\\ExpData\\DataFromOther\\crawl\\extractFromAll\\uid-400\\";

		String weiboId_vecFile=base+"weiboId_avgWord_vector.txt";
		String reviewFormFile=base+"idWeiboIdsReview.txt";

		String dirName="avg"+avgNum+"weibo";
		String destReview=base+dirName+"\\allReview.txt";
		String dest_avgWeibo_vec=base+dirName+"\\word_vector.txt";
		Func.avgWeiboVec(reviewFormFile, weiboId_vecFile, avgNum, destReview, dest_avgWeibo_vec);

	}
	public static void meerUidAndWords() throws IOException
	{
		String base="F:\\ExpData\\DataFromOther\\crawl\\extractFromAll\\";
		Func.mergeUidAndWords(base+"weiboIds.txt", base+"oneLineWeibos_fc.txt",base+"weiboIdAndWeibosFc.txt"," ");


	}
	public static void generateElmentMultiEmbedingInUserSpaceFAvg(int frisVecAvgType) throws Exception
	{
		String base="F:\\ExpData\\DataFromOther\\crawl\\extractFromAll\\uid-400\\selFTnum3_-1\\selSimFris\\simTh0.60(id_sort_w2v)\\";
		String tagVec=base+"weibo(10x20)\\word_vector.txt";
		String uidVec=base+"idFris_forW2v_vector.txt";
		//int frisVecAvgType=1;// 0 for normal avg, just divide fri num; 1 for sim weight avg.

		StackVec stackVec=new StackVec();
		stackVec.loadVecMap(tagVec, 100, " ",StackVec.TAG);
		stackVec.loadVecMap(uidVec, 100, " ", StackVec.UID);

		String idTagsReview=base+"weibo(10x20)\\allReview.txt";

		String en="";
		String idFriReviewDir=""+en;
		String idFrisReview=base+idFriReviewDir+"allReview.txt";
		String expName=en+"weibo(10x20)(Wm.xId_w2v)";
		if(frisVecAvgType==1)
		{
			expName+="(simWeightFvec)";
		}
		String destIdSpecialTagsReview=base+expName+"\\allReview.txt";
		String destSpecialTagsEmbeding=base+expName+"\\word_vector.txt";

		stackVec.computeElementMutiAndWriteEmbedingInUserSpaceFromFile(idTagsReview, idFrisReview, "\t\t", destIdSpecialTagsReview, destSpecialTagsEmbeding, " ",1,frisVecAvgType);	
	}
	public static void generateAvgItemFeatureForSVM() throws IOException
	{
		String base="F:\\ExpData\\DataFromOther\\crawl\\extractFromAll\\uid-400\\";
		String expName="weibo(10x20)(Wm.xId_w2v)";

		String idItemsReview=base+expName+"\\allReview.txt";
		String idVec=base+expName+"\\word_vector.txt";
		String destIdAvgTagsFeature=base+"forSVM\\"+expName+"\\idTag_feature.txt";
		ExtractData.extractUidAvgItemVecForSvm(idItemsReview, "\t\t", "\t", idVec, "\\s{1,}", destIdAvgTagsFeature, "\t");
	}
	public static void generateSimFriByIdw2v_simTh(double simTh) throws IOException
	{
		String base="F:\\ExpData\\DataFromOther\\crawl\\extractFromAll\\uid-400\\selFTnum3_-1\\";
		String idFrisReview=base+"idFrisReview.txt";
		String idVec=base+"idFris_sort_forW2v_vector.txt";
		
		String simThStr=new java.text.DecimalFormat("0.00").format(simTh);
       
		String destSimIdFrisReview=base+"selSimFris\\simTh"+simThStr+"(id_sort_w2v)\\allReview.txt";
		
		ExtractData.extractIdFrisReviewBySimThrehold(idFrisReview, "\t\t", "\t", idVec, "\\s{1,}", destSimIdFrisReview, simTh);
	}
	
	public static void generateSmallIdsetWeiboIdVec() throws IOException
	{
		String base="F:\\ExpData\\DataFromOther\\crawl\\extractFromAll\\";
		String wordVec=base+"all\\oneLineWeibos_fc_vector.txt";
		String uidWeiboIds=base+"uid-400\\selFTnum3_-1\\selSimFris\\simTh0.60(id_sort_w2v)\\uidWeiboIds.txt";
		String weiboIdWeiboFc=base+"all\\weiboIdAndWeibosFc.txt";
		String idSelFrisReview=base+"uid-400\\selFTnum3_-1\\selSimFris\\simTh0.60(id_sort_w2v)\\idFrisReview.txt";
		String destSelIdsWeiboIdVec=base+"uid-400\\selFTnum3_-1\\selSimFris\\simTh0.60(id_sort_w2v)\\weiboId_vector.txt";
		
		ExtractData.generateSmallWeiboIdSetAvgWVecV2(idSelFrisReview, "\t\t","\t",
				uidWeiboIds,"\t", 
				weiboIdWeiboFc,"\\s{1,}", 
				wordVec, "\\s{1,}", 
				destSelIdsWeiboIdVec, " ");
	}
	
	public static void generateUidAvgW(int avgNum) throws IOException
	{
		String base="F:\\ExpData\\DataFromOther\\crawl\\extractFromAll\\";
		String subDir="uid-400\\selFTnum3_-1\\selSimFris\\simTh0.60(id_sort_w2v)\\";
		String idFriReview=base+subDir+"idFrisReview.txt";
		String allIdWeiboIds=base+"all\\uidWeiboIds.txt";
		String destMyIdWeiboIds=base+subDir+"uidWeiboIds.txt";
		//ExtractData.selSmallIsWeiboIds(idFriReview, "\t\t", "\t", allIdWeiboIds, "\t", destMyIdWeiboIds);
		
		String weiboId_vec=base+subDir+"weiboId_vector.txt";
		
		String expName="avg"+avgNum+"weibo";
		String destIdWeiboIds=base+subDir+expName+"\\uidWeiboIds.txt";
		String dest_avgWeibo_vec=base+subDir+expName+"\\weiboId_vector.txt";
		ExtractData.avgWeiboVec(destMyIdWeiboIds, "\t", weiboId_vec, " ", avgNum, destIdWeiboIds, dest_avgWeibo_vec);	
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("hello");
		//mergeSubDirWeibos2OneDirWeibos();
		//excExtractWeibo();
		//excuidWeibos2uidWeiboId();

		//meerUidAndWords();

		//excGenerateWeiboVec();
		//generateAllUidAvgWeiboVec();

		//extractSamllUIdWeiboIds();
		//extractSamllUIdWeibofc();

		//generateWeiboReview();

		//excGenerateAvgXWeibosReview(20);
		

		//excGenerate();
		//FileTool.glanceFileContent("F:\\ExpData\\DataIntegate\\source\\nne\\Weibos\\weiboId_vector.txt", 1, 5);
		//Func.extractNumWeibo("F:\\ExpData\\DataIntegate\\source\\nne\\Weibos\\extractAllWeibo\\avg5weibo\\allReview.txt", "F:\\ExpData\\DataIntegate\\source\\nne\\Weibos\\extractWeibo-20\\avg5weibo\\allReview.txt", 20);

		//Func.extractNumWeiboLineForm("F:\\ExpData\\DataFromOther\\crawl\\extractFromAll\\uid-400\\avg20weibo\\allReview.txt", "F:\\ExpData\\DataFromOther\\crawl\\extractFromAll\\uid-400\\avg20weibo\\extract-10\\allReview.txt", 10);

		//generateElmentMultiEmbedingInUserSpaceFAvg(0);

		//generateAvgItemFeatureForSVM();
		//generateSimFriByIdw2v_simTh(0.6);
		
		//generateSmallIdsetWeiboIdVec();
		
		//generateUidAvgW(20);
		

	}

}
