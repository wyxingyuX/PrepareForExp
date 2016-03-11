package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Bean.WinterHoliday.FriNumUserDatas;
import Bean.WinterHoliday.UserDataFull;
import utils.FileTool;
import utils.IO;
import utils.MyToolKit;
import utils.TestTool;
import func.AvgVec;
import func.ExtractData;
import func.StackVec;
import func.Stat;
import func.WordSetVec;
import other.TempTool;

public class TagExp {
	public static void extractUidTags() throws IOException
	{
		String base="E:\\ExpData\\source\\nne\\";
		List<String> userInfoFiles=new ArrayList<String>();
		String temp="zps\\Sina_NLPIRandTHUext1000_Mute_GenderPre\\";
		userInfoFiles.add(base+temp+"UserInfo0.txt");
		userInfoFiles.add(base+temp+"UserInfo1.txt");
		userInfoFiles.add(base+temp+"UserInfoOfEnterprise1.txt");

		String allUidTags=base+"extractDataFromZps\\allUidTags.txt";
		ExtractData.extractUidTags(userInfoFiles, allUidTags, "\t");

		String ids=base+"extractDataFromZps\\uid_1400.txt";
		String uidTags=base+"extractDataFromZps\\uid_1400_tags.txt";
		ExtractData.extractSelectedUidItems(ids, "\t", allUidTags, "\t", uidTags, "\t");
	}

	public static void extractUidFris() throws IOException{
		String base="E:\\ExpData\\source\\nne\\";
		List<String> userInfoFriendFiles=new ArrayList<String>();
		String temp="zps\\Sina_NLPIRandTHUext1000_Mute_GenderPre\\";
		userInfoFriendFiles.add(base+temp+"UidInfo_friends0.txt");
		userInfoFriendFiles.add(base+temp+"UidInfo_friends1.txt");

		String allUidFris=base+"extractDataFromZps\\allUidFris.txt";
		ExtractData.extractUidFrids(userInfoFriendFiles, allUidFris, "\t");

		String ids=base+"extractDataFromZps\\uid_1400.txt";
		String uidFris=base+"extractDataFromZps\\uid_1400_friIds.txt";
		ExtractData.extractSelectedUidItems(ids, "\t", allUidFris, "\t", uidFris, "\t");
	}

	public static void statUidTagsAndUidFris(int starTagNum,int endTagNum) throws Exception{
		String base="F:\\ExpData\\DataIntegate\\source\\nne\\zps-400-expset\\extract\\selFtagNum3_-1\\";
		String uidTags=base+"uid_400_tag.txt";
		String uidFris=base+"uid_fids.txt";

		FriNumUserDatas friNumUdatas=Stat.generateFriNumUserDatas(uidTags,"\t",uidFris,"\t"); 

		Map<Integer,List<UserDataFull>> fnumUdatasLimitByTagNumMap=Stat.generateFriNumUserDatasByLimtTaggNum(friNumUdatas, starTagNum, endTagNum);
		String dest1=base+"stat\\stat_tagNum"+starTagNum+"_"+endTagNum+".txt";
		Stat.statUserTagsAndUserFris(fnumUdatasLimitByTagNumMap, 0, 50, 5, 
				dest1, true,"\t");
		Stat.statUserTagsAndUserFris(fnumUdatasLimitByTagNumMap, 50, 100, 10, 
				dest1, true,"\t");
		Stat.statUserTagsAndUserFris(fnumUdatasLimitByTagNumMap, 100, 600, 100, 
				dest1, true,"\t");
		Stat.statUserTagsAndUserFris(fnumUdatasLimitByTagNumMap, 600, 2000, 1400, 
				dest1, true,"\t");
		Stat.statOtherInfo(fnumUdatasLimitByTagNumMap, dest1,true,"\t" );
	}
	public static void uniformStepStat(int starTagNum,int endTagNum,int stepFriNum) throws Exception
	{
		String base="F:\\ExpData\\DataIntegate\\source\\nne\\extractDataFromZps\\selSatisfiedUser-tag3_-1_friTag3_-1\\selSatisfiedUser\\";
		String uidTags=base+"uidTags_satisfy_TnFn.txt";
		String uidFris=base+"uidFris_satisfy_TnFnFTn.txt";


		FriNumUserDatas friNumUdatas=Stat.generateFriNumUserDatas(uidTags,"\t",uidFris,"\t"); 
		Map<Integer,List<UserDataFull>> fnumUdatasLimitByTagNumMap=Stat.generateFriNumUserDatasByLimtTaggNum(friNumUdatas, starTagNum, endTagNum);

		String dest=base+"stat_tagNum"+starTagNum+"_"+endTagNum+"_friNumStep"+stepFriNum+".txt";
		Stat.statUserTagsAndUserFris(fnumUdatasLimitByTagNumMap, 0, 600, stepFriNum, 
				dest, true,"\t");
		Stat.statOtherInfo(fnumUdatasLimitByTagNumMap, dest,true,"\t" );
	}

	public static void stat() throws Exception{
		statUidTagsAndUidFris(1,-1);
		statUidTagsAndUidFris(1,3);
		statUidTagsAndUidFris(3,-1);
		//uniformStepStat(3,-1,100);
		statUidTagsAndUidFris(3,5);
		statUidTagsAndUidFris(5,-1);

	}
	public static void selectUserSatisfy(int startTagNum,int endTagNum,int startFriNum,int endFriNum,int writeTotalSize) throws Exception{
		String base="F:\\ExpData\\DataIntegate\\source\\nne\\zps-400-expset\\";
		String uidTags=base+"data\\uid_400_tag.txt";
		String uidFris=base+"extract\\selFtagNum3_-1"+"\\uid_fids.txt";

		FriNumUserDatas friNumUdatas=Stat.generateFriNumUserDatas(uidTags,"\t",uidFris,"\t"); 

		Map<Integer,List<UserDataFull>> fnumUdatasLimitByTagNumMap=Stat.generateFriNumUserDatasByLimtTaggNum(friNumUdatas, startTagNum, endTagNum);
		Map<Integer,List<UserDataFull>> fnumUdatasLTnFn= Stat.generaterFriNumUserDatasByLimitFriNum(fnumUdatasLimitByTagNumMap, startFriNum, 
				endFriNum);

		String selDirName="selTag"+startTagNum+"_"+endTagNum+"_fnum"+startFriNum+"_"+endFriNum;
		String destUidTagsLimitTnFn=base+selDirName+"\\uidtags.txt";
		String destUidFrisLimitTnFn=base+selDirName+"\\uidfris.txt";
		IO.writeFriNumUdatasMap2File(fnumUdatasLTnFn, writeTotalSize, destUidTagsLimitTnFn, "\t", destUidFrisLimitTnFn, "\t");

		//		String destFile1=base+"selSatisfiedUser\\uidsOfUidTags.txt";
		//		String destFile2=base+"selSatisfiedUser\\tagsOfUidTags.txt";
		//		MyToolKit.splitFileElms2Files(destUidTagsLimitTnFn, "\t", 1, destFile1, destFile2, "\t");
		//
		//		String destFile3=base+"selSatisfiedUser\\uidsOfUidFris.txt";
		//		String destFile4=base+"selSatisfiedUser\\frisOfUidFris.txt";
		//		MyToolKit.splitFileElms2Files(destUidFrisLimitTnFn, "\t", 1, destFile3, destFile4, "\t");
	}

	public static void selectFriTagNumSatisfy(int minTagNum,int maxTagNum) throws Exception
	{
		String base="F:\\ExpData\\DataIntegate\\source\\nne\\zps-400-expset\\";
		String uidFris=base+"data\\uid_400_friends.txt";
		String allUidTags=base+"data\\uidtag.txt";
		String destUidFris=base+"extract\\selFtagNum"+minTagNum+"_"+maxTagNum+"\\uid_fids.txt";

		ExtractData.extractFrisFromUidFrisSatisfy(uidFris,"\t",allUidTags,"\t",minTagNum,maxTagNum,destUidFris,"\t");
	}

	public static void generateBalanceUserSet(int cateSize) throws Exception{
		String base="F:\\ExpData\\DataIntegate\\source\\nne\\extractDataFromZps\\selSatisfiedUser-tag3_-1_friTag3_-1\\selSatisfiedUser\\";
		String allIdCate=base+"allIdCate.txt";
		String satisfyUidTags=base+"uidTags_satisfy_TnFn.txt";
		String satisfyUidFris=base+"uidFris_satisfy_TnFn.txt";

		ExtractData.generateBalanceUserSet(satisfyUidTags, "\t",
				satisfyUidFris, "\t", 
				allIdCate, "\t", cateSize);
	}

	public static void generateAllIdCate() throws IOException{
		String base="F:\\ExpData\\DataIntegate\\source\\nne\\zps-400-expset\\";
		String maleFile=base+"data\\male.txt";
		String femaleFile=base+"data\\female.txt";
		String idCatesFile=base+"extract\\idCate.txt";
		MyToolKit.mergeGenderFile(femaleFile, maleFile, idCatesFile, "\t");
	}
	public static void generateExpSet() throws IOException{
		// TODO Auto-generated method stub
		String base="F:\\ExpData\\DataIntegate\\source\\nne\\zps-400-expset\\extract\\selFtagNum3_-1\\";
		//String allIdCateFile=base+"allIdCate.txt";
		//String smallIdSetFile=base+"uidTags_satisfy_TnFn_balanceCate_2x380.txt";
		String samllIdCateFile=base+"idCate.txt";
		//ExtractData.generateSamllIdSetCate(allIdCateFile, "\t", smallIdSetFile, "\t", samllIdCateFile, "\t");

		int fold=5;
		MyToolKit.generaterFoldTrainValidTestExpSet(samllIdCateFile, "\t", base+fold+"fold\\", fold);
	}



	public static void generateUidFridTags() throws Exception{
		String base="F:\\ExpData\\DataIntegate\\source\\nne\\zps-400-expset\\";
		String uidFriIds=base+"extract\\selFtagNum3_-1\\uid_fids.txt";
		String allUidTags=base+"data\\uidtag.txt";
		String dest=base+"extract\\selFtagNum3_-1\\ufid_tags.txt";
		ExtractData.extractUidFriIdTags(uidFriIds, "\t", allUidTags, "\t", dest, "\t");
		MyToolKit.filterSameIdLine(dest, dest, "\t");

		//split
		//		String splitUidFriIds=base+"selSatisfiedUser-tag3_-1_friTag3_-1\\selSatisfiedUser\\forReview\\split_UidFris.txt";
		//		String splitTagsOfUidFriIds=base+"selSatisfiedUser-tag3_-1_friTag3_-1\\selSatisfiedUser\\forReview\\split_Tags_OfUidAndFris.txt";
		//		MyToolKit.splitFileElms2Files(dest, "\t", 1, splitUidFriIds, splitTagsOfUidFriIds, "\t");
	}
	public static void generateUidFriIds() throws IOException
	{
		String base="F:\\ExpData\\DataIntegate\\source\\nne\\zps-400-expset\\extract\\selFtagNum3_-1\\";
		String uidFriIds=base+"uid_fids.txt";
		String dest=base+"ufids_multLine.txt";
		ExtractData.generateUidFidMultiLine(uidFriIds, "\t", dest, "\t");
	}

	public static void generateAllUidAvgTagsVec() throws IOException{
		String base="F:\\ExpData\\DataIntegate\\source\\nne\\zps-400-expset\\";
		String uidAndFriIdTags=base+"extract\\selFtagNum3_-1\\ufid_tags.txt";
		String tagsWordVec=base+"data\\friendsTag_vector.txt";

		WordSetVec tagsVec=new WordSetVec();
		tagsVec.loadWordVecMaxtrix(tagsWordVec, "\\s{1,}");
		String userIdVec=base+"extract\\selFtagNum3_-1\\id_avgTags_vector.txt";
		tagsVec.generateWordsVecAvg(uidAndFriIdTags, "\t", userIdVec, " ");
	}
	public static void filterSameUidAvgTagVec() throws IOException
	{
		String base="F:\\ExpData\\DataIntegate\\source\\nne\\extractDataFromZps\\selSatisfiedUser-tag3_-1_friTag3_-1\\selSatisfiedUser\\forReview\\";
		String userIdVec=base+"uidAndFri_vector.txt";
		String destIdValueLine=base+"uidAndFri_filterSameId_vector.txt";
		MyToolKit.filterSameIdLineForVecFile(userIdVec, destIdValueLine, " ");
	}
	public static void generateUidTagReview() throws IOException{
		String base="F:\\ExpData\\DataIntegate\\source\\nne\\zps-400-expset\\extract\\selFtagNum3_-1\\";
		String allUidTags=base+"ufid_tags.txt";
		String idCate=base+"idCate.txt";
		String dest=base+"idTagsReview.txt";
		ExtractData.generateUidItemsReviews(allUidTags, "\t", idCate, "\t", dest);
	}
	public static void generateUidFrisReview() throws IOException{
		String base="F:\\ExpData\\DataIntegate\\source\\nne\\zps-400-expset\\extract\\selFtagNum3_-1\\";
		String idcate=base+"idCate.txt";
		String uidFriIds=base+"idFris_sort_forW2v.txt";
		String dest=base+"idFrisReview_sort.txt";
		ExtractData.generateUidItemsReviews(uidFriIds, "\t", idcate, "\t", dest);
	}

	public static void generateEmbedingInUserSpace() throws Exception{
		String base="F:\\ExpData\\DataIntegate\\source\\nne\\zps-400-expset\\extract\\selFtagNum3_-1\\";
		String tagsVec=base+"friendsTag_vector.txt";
		String uidVec=base+"id_avgTags_vector.txt";

		StackVec stackVec=new StackVec();
		stackVec.loadVecMap(tagsVec, 100, " ",StackVec.TAG);
		stackVec.loadVecMap(uidVec, 100, " ", StackVec.UID);

		String uidTagsReview=base+"idTagsReview.txt";
		String uidFriIdsReview=base+"idFrisReview.txt";

		String temp="tag(TmxId_avgT)_";
		String destEmbeding_MaxFriNum=base+temp+"maxFriNum\\"+"word_vector.txt";
		String destUidSpecficTagsReview_MaxFriNum=base+temp+"maxFriNum\\"+"allReview.txt";
		int maxFriNum=MyToolKit.getMaxItemNumInPosElm(uidFriIdsReview, "\t\t", 3, "\t");
		stackVec.computeAndWriteEmbedingInUserSpaceFromFile(uidTagsReview, uidFriIdsReview, "\t\t", destEmbeding_MaxFriNum, " ",maxFriNum, destUidSpecficTagsReview_MaxFriNum);

		String destEmbeding_MinFriNum=base+temp+"minFriNum\\"+"word_vector.txt";
		String destUidSpecficTagsReview_MinFriNum=base+temp+"minFriNum\\"+"allReview.txt";
		int minFriNum=MyToolKit.getMinItemNumInPosElm(uidFriIdsReview, "\t\t", 3, "\t");
		stackVec.computeAndWriteEmbedingInUserSpaceFromFile(uidTagsReview, uidFriIdsReview, "\t\t", destEmbeding_MinFriNum, " ",minFriNum, destUidSpecficTagsReview_MinFriNum);

		String destEmbeding_AvgFriNum=base+temp+"avgFriNum\\"+"word_vector.txt";
		String destUidSpecficTagsReview_AvgFriNum=base+temp+"avgFriNum\\"+"allReview.txt";
		int avgFriNum=MyToolKit.getAvgItemNumInPosElm(uidFriIdsReview, "\t\t", 3, "\t");
		stackVec.computeAndWriteEmbedingInUserSpaceFromFile(uidTagsReview, uidFriIdsReview, "\t\t", destEmbeding_AvgFriNum, " ",avgFriNum, destUidSpecficTagsReview_AvgFriNum);

	}

	public static void concateEmbeding(String embedingInTagSpaceName,String embedingInUserSpaceName) throws Exception
	{
		String base="F:\\ExpData\\DataIntegate\\source\\nne\\publicInfo3\\tag3_-1_friTag3_-1_fri10_-1_2X380\\";
		String subWordVecFilePath="\\wordVec\\word_vector.txt";


		String embedingInTagSpace=base+embedingInTagSpaceName+subWordVecFilePath;
		String embedingInUserSpace=base+embedingInUserSpaceName+subWordVecFilePath;
		String destConcateVec=base+embedingInTagSpaceName+"_"+embedingInUserSpaceName+subWordVecFilePath;
		MyToolKit.wordVecConcate(embedingInTagSpace, embedingInUserSpace, destConcateVec, " ");

		String subWordReviewFilePath="\\textData\\allReview.txt";
		String reviewInUserSpace=base+embedingInUserSpaceName+subWordReviewFilePath;
		String destWordReview=base+embedingInTagSpaceName+"_"+embedingInUserSpaceName+subWordReviewFilePath;
		FileTool.copy(reviewInUserSpace, destWordReview);
	}

	public static void generateDiemEmbedingInUserSpace() throws Exception
	{
		String base="F:\\ExpData\\DataIntegate\\source\\nne\\extractDataFromZps\\selSatisfiedUser-tag3_-1_friTag3_-1\\selSatisfiedUser\\forReview\\";
		String allUidTags=base+"uidFrisTags_satisfy_TnFn.txt";
		String tagsVec=base+"split_Tags_OfUidAndFris_vector.txt";

		AvgVec av=new AvgVec(allUidTags,"\t",tagsVec,"\\s{1,}");

		String uidTagsReview=base+"idTagsReview.txt";
		String uidFriIdsReview=base+"idFrisReview.txt";
		String destEmbeding=base+"DiemEmbedingInAvgUserSpace\\diemEmbeding.txt";
		String destUidSpecficTagsReview=base+"DiemEmbedingInAvgUserSpace\\uidSpecficTagDiemEmbedingReview.txt";
		int embedingSize=MyToolKit.getVecDiem(tagsVec, "\\s{1,}");
		av.computeAndWriteDiemEmbeingInAvgUserSpace(uidTagsReview, uidFriIdsReview, "\t\t", destEmbeding, " ", embedingSize, destUidSpecficTagsReview);

	}

	public static void generateUidTagsAddFriTgsReviews() throws IOException
	{
		String base="F:\\ExpData\\DataIntegate\\source\\nne\\extractDataFromZps\\selSatisfiedUser-tag3_-1_friTag3_-1\\selSatisfiedUser\\forReview\\";
		String uidFrisReview=base+"idFrisReview.txt";
		String uidTags=base+"uidFrisTags_satisfy_TnFn.txt";
		String destUidTagsAddFriTagsReview=base+"idTagsAddFriTagsReview.txt";
		String destUidTagsAddFritags=base+"idTagsAddFrisTags.txt";

		ExtractData.generateUidTagsAddFriTgsReviews(uidFrisReview, "\t\t", "\t", uidTags, "\t", destUidTagsAddFriTagsReview,destUidTagsAddFritags);

	}

	public static void generateUidIdAddFriIdsReviews() throws IOException
	{
		String base="F:\\ExpData\\DataIntegate\\source\\nne\\extractDataFromZps\\selSatisfiedUser-tag3_-1_friTag3_-1\\selSatisfiedUser\\forReview\\";
		String uidFrisReview=base+"idFrisReview.txt";
		String destUidIdAddFriIdsReview=base+"idUidAddFriIdsReview.txt";

		ExtractData.generateUididAddFriIdsReviews(uidFrisReview, "\t\t", "\t", destUidIdAddFriIdsReview);
	}

	public static void generateElmentMultiEmbedingInUserSpaceFAvg(int frisVecAvgType) throws Exception
	{
		String base="F:\\ExpData\\DataIntegate\\source\\nne\\zps-400-expset\\extract\\selFtagNum3_-1\\";
		String tagVec=base+"friendsTag_vector.txt";
		String uidVec=base+"id_avgTags_vector.txt";
		//int frisVecAvgType=1;// 0 for normal avg, just divide fri num; 1 for sim weight avg.

		StackVec stackVec=new StackVec();
		stackVec.loadVecMap(tagVec, 100, " ",StackVec.TAG);
		stackVec.loadVecMap(uidVec, 100, " ", StackVec.UID);

		String idTagsReview=base+"idTagsReview.txt";

		String en="simTp50(id_avgT)\\";
		String idFriReviewDir="selSimFris\\"+en;
		String idFrisReview=base+idFriReviewDir+"idFrisReview.txt";
		String expName=en+"tag(Tm.xId_avgT)";
		if(frisVecAvgType==1)
		{
			expName+="(simWeightFvec)";
		}
		String destIdSpecialTagsReview=base+expName+"\\allReview.txt";
		String destSpecialTagsEmbeding=base+expName+"\\word_vector.txt";

		stackVec.computeElementMutiAndWriteEmbedingInUserSpaceFromFile(idTagsReview, idFrisReview, "\t\t", destIdSpecialTagsReview, destSpecialTagsEmbeding, " ",1,frisVecAvgType);	
	}

	public static void generateElmentMultiEmbedingInUserSpaceFTAvg() throws Exception
	{
		String base="F:\\ExpData\\DataIntegate\\source\\nne\\extractDataFromZps\\selSatisfiedUser-tag3_-1_friTag3_-1\\selSatisfiedUser\\forReview\\";
		String tagsVec=base+"split_Tags_OfUidAndFris_vector.txt";
		String allUidTags=base+"uidFrisTags_satisfy_TnFn.txt";
		AvgVec avgVec=new AvgVec(allUidTags,"\t",tagsVec,"\\s{1,}");


		String idTagsReview=base+"idTagsReview.txt";
		String idFrisReview=base+"idFrisReview.txt";
		String expName="ElmentMultiEmbedingInUserSpace(FTAvg)";

		String destIdSpecialTagsReview=base+expName+"\\idSpecialTagsReview.txt";
		String destEmbeding=base+expName+"\\specialTags_vector.txt";

		avgVec.computeElementMutiAndWriteEmbedingInUserSpaceFromFile(idTagsReview, idFrisReview, "\t\t", destIdSpecialTagsReview, destEmbeding, " ",1);	
	}
	public static void extractUidFrisForW2v() throws IOException
	{
		String base="F:\\ExpData\\DataIntegate\\source\\nne\\extractDataFromZps\\selSatisfiedUser-tag3_-1_friTag3_-1\\selSatisfiedUser\\forReview\\";
		String idFrisReview=base+"idFrisReview.txt";
		String destIdFris=base+"idFris.txt";
		ExtractData.extractUidAndFriIds(idFrisReview, "\t\t", "\t", destIdFris);
	}
	public static void generateUidFrisForIdW2v() throws IOException
	{
		String base="F:\\ExpData\\DataIntegate\\source\\nne\\zps-400-expset\\extract\\selFtagNum3_-1\\";
		String idFris=base+"uid_fids.txt";
		String destIdFris=base+"idFris_forW2v.txt";
		ExtractData.generateIdAndFrisIdsForIdW2v(idFris, "\t", destIdFris);
	}

	public static void generaterFeatureForSvm() throws IOException
	{
		String base="F:\\ExpData\\DataIntegate\\source\\nne\\zps-400-expset\\extract\\selFtagNum3_-1\\";
		String idTagsReview=base+"idFrisReview.txt";
		String idAvgTagsVec=base+"idFris_forW2v_vector.txt";
		String destIdAvgTagsFeature=base+"forSVM\\id_w2v\\id_feature.txt";
		ExtractData.extractUidVecForSvm(idTagsReview, "\t\t", idAvgTagsVec, " ", destIdAvgTagsFeature, "\t");

	}
	public static void generateAvgItemFeatureForSVM() throws IOException
	{
		String base="F:\\ExpData\\DataIntegate\\source\\nne\\zps-400-expset\\extract\\selFtagNum3_-1\\";
		String expName="id_w2v_sort";

		String idItemsReview=base+expName+"\\allReview.txt";
		String idVec=base+expName+"\\word_vector.txt";
		String destIdAvgTagsFeature=base+"forSVM\\"+expName+"\\id_feature.txt";
		ExtractData.extractUidAvgItemVecForSvm(idItemsReview, "\t\t", "\t", idVec, "\\s{1,}", destIdAvgTagsFeature, "\t");
	}

	public static void appendVal2Train() throws IOException
	{
		String base="E:\\OExpWorkSpace\\curWork\\SeniorSister\\FeatureVecProcess\\dest\\expSet-zps_400_data(FTnum3_-1)-addValSet\\";
		int foldNum=5;
		for(int i=0;i<foldNum;++i)
		{
			String dir=base+i+"\\";
			FileTool.appendFileA2FileB(dir+"validation.txt", dir+"train.txt");
			FileTool.remove(dir+"validation.txt");
		}
	}

	public static void prepareFidNeedCrawl() throws Exception
	{
		String base="F:\\ExpData\\DataIntegate\\source\\nne\\zps-400-expset\\data\\";
		String fidTags=base+"fidTags_6w.txt";

		String fid=base+"forCrawl\\fidNeedCrawl_slice5.txt";
		String ftags=base+"forCrawl\\tags_forFid.txt";
		MyToolKit.splitFileElms2Files(fidTags, "\\s{1,}", 1, fid, ftags, "\t");
		FileTool.cutFile2Slices(fid, 2);
	}

	public static void test() throws IOException
	{
		String base="F:\\ExpData\\DataIntegate\\source\\nne\\zps-400-expset\\data\\forCrawl\\";

		String allId=base+"fidNeedCrawl.txt";
		List<String> haveAssign=new ArrayList<String>();
		haveAssign.add(base+"fidNeedCrawl_slice1.txt");
		haveAssign.add(base+"fidNeedCrawl_slice2.txt");
		haveAssign.add(base+"fidNeedCrawl_slice3.txt");
		List<String> newAssign=new ArrayList<String>();
		newAssign.add(base+"fidNeedCrawl_slice4_slice1.txt");
		newAssign.add(base+"fidNeedCrawl_slice4_slice2.txt");
		newAssign.add(base+"fidNeedCrawl_slice5_slice1.txt");
		newAssign.add(base+"fidNeedCrawl_slice5_slice2.txt");
		TempTool.god(allId, haveAssign, newAssign);
	}

	public static void generateUTag() throws Exception
	{
		String base="F:\\ExpData\\DataIntegate\\source\\nne\\zps-400-expset\\data\\";
		String uids=base+"uid_400.txt";
		String allUidTags=base+"uidtag.txt";
		String destUtag=base+"uid_400_tag.txt";
		ExtractData.generateUidTags(uids, "\t", allUidTags, "\t", destUtag, "\t");
	}
	public static void generateClusterFri(int clusterStep) throws IOException
	{
		String base="F:\\ExpData\\DataIntegate\\source\\nne\\zps-400-expset\\extract\\selFtagNum3_-1\\";
		String idFirsReview=base+"idFrisReview.txt";
		String idVec=base+"idFris_sort_forW2v_vector.txt";

		String expName="id_w2v_sort_cluster(step"+clusterStep+")";
		String destIdClusterFriReview=base+expName+"\\allReview.txt";
		String destClusterFriVec=base+expName+"\\word_vector.txt";

		ExtractData.generateClusterFri(idFirsReview, "\t\t", "\t", idVec, " ", destIdClusterFriReview, destClusterFriVec, clusterStep);
	}

	public static void generateSimFriThTp(double simTh,int topN) throws IOException
	{
		String base="F:\\ExpData\\DataIntegate\\source\\nne\\zps-400-expset\\extract\\selFtagNum3_-1\\";
		String idFrisReview=base+"idFrisReview.txt";

		String idW2vVec=base+"idFris_forW2v_vector.txt";
		String idAvgTVec=base+"id_avgTags_vector.txt";	

		String tmpIwName="(id_w2v)";
		String tmpIavtName="(id_avgT)";


		//double simTh=0.5;
		String sim1Vec=idW2vVec;

		//int topN=10;
		String sim2Vec=idAvgTVec;

		String sim1VecName="";
		String sim2VecName="";
		if(sim1Vec==idW2vVec&&sim2Vec==idAvgTVec)
		{
			sim1VecName=tmpIwName;
			sim2VecName=tmpIavtName;
		}
		if(sim2Vec==idW2vVec&&sim1Vec==idAvgTVec)
		{
			sim2VecName=tmpIwName;
			sim1VecName=tmpIavtName;

		}

		String selSimFriDir="selSimFris\\";

		String simThStr=new java.text.DecimalFormat("0.00").format(simTh);
		String sim1Name="simTh"+simThStr+sim1VecName;
		String destSim1IdFrisReview=base+selSimFriDir+sim1Name+"\\idFrisReview.txt";
		ExtractData.extractIdFrisReviewBySimThrehold(idFrisReview, "\t\t", "\t", sim1Vec, "\\s{1,}", destSim1IdFrisReview, simTh);

		String sim2Name="simTp"+topN+sim2VecName;
		String destSim2IdFrisReview=base+selSimFriDir+sim1Name+"_"+sim2Name+"\\idFrisReview.txt";
		ExtractData.extractIdFrisReviewByTopn(destSim1IdFrisReview, "\t\t", "\t", sim2Vec, "\\s{1,}", destSim2IdFrisReview, topN);
	}

	public static void generateSimFriTpTp(int topN1,int topN2) throws IOException
	{
		String base="F:\\ExpData\\DataIntegate\\source\\nne\\zps-400-expset\\extract\\selFtagNum3_-1\\";
		String idFrisReview=base+"idFrisReview.txt";

		String idW2vVec=base+"idFris_forW2v_vector.txt";
		String idAvgTVec=base+"id_avgTags_vector.txt";	

		String tmpIwName="(id_w2v)";
		String tmpIavtName="(id_avgT)";


		//int topN1=20;
		String sim1Vec=idAvgTVec;

		//int topN2=10;
		String sim2Vec=idW2vVec;

		String sim1VecName="";
		String sim2VecName="";
		if(sim1Vec==idW2vVec&&sim2Vec==idAvgTVec)
		{
			sim1VecName=tmpIwName;
			sim2VecName=tmpIavtName;
		}
		if(sim2Vec==idW2vVec&&sim1Vec==idAvgTVec)
		{
			sim2VecName=tmpIwName;
			sim1VecName=tmpIavtName;

		}

		String selSimFriDir="selSimFris\\";

		String sim1Name="simTp"+topN1+sim1VecName;
		String destSim1IdFrisReview=base+selSimFriDir+sim1Name+"\\idFrisReview.txt";
		ExtractData.extractIdFrisReviewByTopn(idFrisReview, "\t\t", "\t", sim1Vec, "\\s{1,}", destSim1IdFrisReview, topN1);

		String sim2Name="simTp"+topN2+sim2VecName;
		String destSim2IdFrisReview=base+selSimFriDir+sim1Name+"_"+sim2Name+"\\idFrisReview.txt";
		ExtractData.extractIdFrisReviewByTopn(destSim1IdFrisReview, "\t\t", "\t", sim2Vec, "\\s{1,}", destSim2IdFrisReview, topN2);
	}
	public static void generateSortFris() throws IOException
	{
		String base="F:\\ExpData\\DataIntegate\\source\\nne\\zps-400-expset\\extract\\selFtagNum3_-1\\";
		String idFris=base+"uid_fids.txt";
		String sortIdFris=base+"idFris_sort_forW2v.txt";
		
		ExtractData.generateSortIds(idFris, "\t", sortIdFris, "\t");
	}
	//	public static void generateSimFriAuto() throws IOException
	//	{
	//		String base="F:\\ExpData\\DataIntegate\\source\\nne\\zps-400-expset\\extract\\selFtagNum3_-1\\";
	//		String idFrisReview=base+"idFrisReview.txt";
	//		
	//		String idW2vVec=base+"idFris_forW2v_vector.txt";
	//		String idAvgTVec=base+"id_avgTags_vector.txt";	
	//		
	//		
	//		String selSimFriDir="selSimFris\\";
	//		double simTh=0.7;
	//		String simThStr=String.format(".%2f", simTh);
	//		String sim1Name="simTh+"+simThStr+"(id_w2v)";
	//		String destSim1IdFrisReview=base+selSimFriDir+sim1Name+"\\idFrisReview.txt";
	//		ExtractData.extractIdFrisReviewBySimThrehold(idFrisReview, "\t\t", "\t", idW2vVec, "\\s{1,}", destSim1IdFrisReview, simTh);
	//		int topN=10;
	//		String sim2Name="simTp"+topN+"(id_avgT)";
	//		String destSim2IdFrisReview=base+selSimFriDir+sim1Name+"_"+sim2Name+"\\idFrisReview.txt";
	//		ExtractData.extractIdFrisReviewByTopn(destSim1IdFrisReview, "\t\t", "\t", idAvgTVec, "\\s{1,}", destSim2IdFrisReview, 10);
	//	}

	public static void holidayWorkFlow()
	{

		// TODO Auto-generated method stub
		//extractUidTags();
		//extractUidFris();
		//stat();

		//generateAllIdCate();
		//		selectUserSatisfy(3,-1,10,-1,-1);
		//		generateBalanceUserSet(0);
		//		generateExpSet();

		//		TestTool.printCateNum("E:\\ExpData\\source\\nne\\extractDataFromZps\\selSatisfiedUser\\idCate.txt", "\t");

		//		generateUidFridTags();
		//		generateAllUidAvgTagsVec();
		//		generateUidTagReview();
		//		generateUidFrisReview();
		//		generateEmbedingInUserSpace();

		//Stat.countNoVecUserNum("E:\\ExpData\\source\\nne\\extractDataFromZps\\selSatisfiedUser\\forReview\\uidAndFri_vector.txt"," ");
		//		
		//		concateEmbeding("Tag-friTagVec","Tag-tagVecInUserSpace-maxFri");
		//		concateEmbeding("Tag-friTagVec","Tag-tagVecInUserSpace-avgFri");
		//		concateEmbeding("Tag-friTagVec","Tag-tagVecInUserSpace-minFri");

	}
	
	public static void tagWorkFlow()
	{

		//generateAllIdCate();
		//generateUTag();

		//selectFriTagNumSatisfy(3,-1);
		//selectUserSatisfy(3,-1,10,-1,-1);
		//stat();

		//generateBalanceUserSet(0);
		//generateExpSet();

		//	TestTool.printCateNum("E:\\ExpData\\source\\nne\\extractDataFromZps\\selSatisfiedUser\\idCate.txt", "\t");

		//generateUidFrisForIdW2v();
		//		generateUidFriIds();
		//generateUidFridTags();
		//generateAllUidAvgTagsVec();
		//filterSameUidAvgTagVec();

		//generateUidTagReview();
		//generateUidFrisReview();

		//generateSimFriThTp(0.9,10);
		//generateSimFriTpTp(50,10);

		//generateEmbedingInUserSpace();
		//generateDiemEmbedingInUserSpace();
		// generateElmentMultiEmbedingInUserSpaceFAvg(0);// 0 for normal avg, just divide fri num; 1 for sim weight avg.
		//generateElmentMultiEmbedingInUserSpaceFTAvg();

		//Stat.countNoVecUserNum("F:\\ExpData\\DataIntegate\\source\\nne\\extractDataFromZps\\selSatisfiedUser-tag3_-1_friTag3_-1\\selSatisfiedUser\\forReview\\uidAndFri_vector.txt"," ");
		//		
		//				concateEmbeding("Tag-friTagVec","Tag-tagVecInUserSpace-maxFri");
		//				concateEmbeding("Tag-friTagVec","Tag-tagVecInUserSpace-avgFri");
		//				concateEmbeding("Tag-friTagVec","Tag-tagVecInUserSpace-minFri");

		//concateEmbeding("Tag-friTagVec","Tag-tagVecElementMultiInUserSpace");

		//generateUidTagsAddFriTgsReviews();
	//generateUidIdAddFriIdsReviews();

		//extractUidFrisForW2v();
		//generaterFeatureForSvm();
		//generateAvgItemFeatureForSVM();
		//appendVal2Train();

		//generateClusterFri(10);
		
	}

	public static void main(String[] args) throws Exception 
	{
		
		//generateSortFris();
		//generateClusterFri(10);
		//generateAvgItemFeatureForSVM();
//		ExtractData.concateUidClusterAndUidTags("F:\\ExpData\\DataFromOther\\crawl\\extractFromAll\\uid-400\\selFTnum3_-1\\idFris_sort_forW2v_vector_cluster.txt", "\\s{1,}",
//				"F:\\ExpData\\DataIntegate\\source\\nne\\extractDataFromZps\\allUidTags.txt", "\t",
//				"F:\\ExpData\\DataFromOther\\crawl\\extractFromAll\\uid-400\\selFTnum3_-1\\idFris_sort_forW2v_vector_cluster.tag.txt");
//	
	}

}
