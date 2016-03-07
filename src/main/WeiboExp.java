package main;

import java.io.IOException;

import func.Func;
import func.UserVec;
import func.WeiboVec;

public class WeiboExp {      
	public static void excExtractWeibo() throws IOException{ 
		String jsonDataDir="F:\\ExpData\\DataIntegate\\source\\nne\\Weibos\\Weibos\\";
		String weibosFilePath="F:\\ExpData\\DataIntegate\\source\\nne\\Weibos\\uidWeibos.txt";
		Func.json2Weibos(jsonDataDir, weibosFilePath, "\t");

		String uid="F:\\ExpData\\DataIntegate\\source\\nne\\Weibos\\uid.txt";
		String weibo="F:\\ExpData\\DataIntegate\\source\\nne\\Weibos\\weibos.txt";
		Func.split(weibosFilePath, "\t", 1, uid, weibo);
		String oneWeiboOneLineFilePath="F:\\ExpData\\DataIntegate\\source\\nne\\Weibos\\oneWeiboOneLine.txt";
		Func.oneWeiboOneLine(weibo, "\t", oneWeiboOneLineFilePath);
	}
	public static void excuidWeibos2uidWeiboId() throws IOException{
		String uidWeibos="F:\\ExpData\\DataIntegate\\source\\nne\\Weibos\\uidWeibos.txt";
		String uidWeiboIds="F:\\ExpData\\DataIntegate\\source\\nne\\Weibos\\uidWeiboIds.txt";
		Func.uidWeibos2uidWeiboId(uidWeibos, "\t", uidWeiboIds, "\t");
		String weiboIdAndWeibos="F:\\ExpData\\DataIntegate\\source\\nne\\Weibos\\weiboIdAndWeibos.txt";
		String weiboIds="F:\\ExpData\\DataIntegate\\source\\nne\\Weibos\\weiboIds.txt";
		String oneLineWeibos="F:\\ExpData\\DataIntegate\\source\\nne\\Weibos\\oneLineWeibos.txt";
		Func.split(weiboIdAndWeibos, "\t", 1, weiboIds, oneLineWeibos);
	}

	public static void excGenerateWeiboVec() throws IOException{
		String base="F:\\ExpData\\DataIntegate\\source\\nne\\Weibos\\";
		String weiboIdAndWeiboFc=base+"weiboIdAndWeibosFc.txt";
		String weiboFcVecFile=base+"oneLineWeibosFc_vector.txt";

		WeiboVec weiboVec=new WeiboVec();
		weiboVec.loadWordVecMaxtrix(weiboFcVecFile, "\\s{1,}");
		String weiboVecFile=base+"weiboId_vector.txt";
		weiboVec.generateWeiboVecAvg(weiboIdAndWeiboFc, "\\s{1,}", weiboVecFile, " ");
	}

	public static void excGenerateUserVec() throws IOException{
		String base="F:\\ExpData\\DataIntegate\\source\\nne\\";
		String femaleIds=base+"Tags\\female.txt";
		String maleIds=base+"Tags\\male.txt";
		String wordVecFilePath=base+"Weibos\\weibosFc_vector.txt";
		String uidWordFilePath=base+"Weibos\\uidWeibosFc.txt";
		String userVecFilePath=base+"Weibos\\userLabelVec_400.txt";

		UserVec uvec=new UserVec();
		uvec.loadIdsLabel(femaleIds,maleIds);
		uvec.loadWordVecMaxtrix(wordVecFilePath, "\\s{1,}");
		uvec.generateUserVecAvg(uidWordFilePath, "\\s{1,}", userVecFilePath, "\t");
	}
    public static void excGenerateUserVecLineForm() throws IOException{
		String base="F:\\ExpData\\DataIntegate\\source\\nne\\Weibos\\extractWeibo-10\\avg20weiboLineForm\\";
		String wordVecFilePath=base+"weiboId_vector.txt";
		String uidWordFilePath=base+"allReview.txt";
		String userVecFilePath=base+"weibo(10x20)_avg_feature.txt";

		UserVec uvec=new UserVec();
		uvec.loadWordVecMaxtrix(wordVecFilePath, "\\s{1,}");
		uvec.generateUserVecAvgLineForm(uidWordFilePath, "\t", userVecFilePath, "\t");
	
    }
	public static void excGenerateTrainAndTest() throws IOException{
		String base="F:\\ExpData\\DataIntegate\\source\\nne\\";
		String femaleIds=base+"Tags\\female.txt";
		String maleIds=base+"Tags\\male.txt";
		String temp="Weibos\\";
		String uidLableVecFile=base+temp+"userLabelVec_400.txt";
		String trainFile=base+temp+"train\\trainData.txt";
		String testFile=base+temp+"test\\testData.txt";

		UserVec uvec=new UserVec();
		uvec.loadIdsLabel(femaleIds,maleIds);
		uvec.generateTrainAndTest(uidLableVecFile, "\t", 0.8f, trainFile, testFile);
	}
	public static void excGenerate() throws IOException{
		UserVec uvec=new UserVec();
		String base="F:\\ExpData\\DataIntegate\\source\\nne\\";
		String temp="Weibos";
		String femaleIds=base+"Tags\\female.txt";
		String maleIds=base+"Tags\\male.txt";
		uvec.loadIdsLabel(femaleIds,maleIds);
		uvec.generateSortIdSourceLabelWordsTrainAndTest(base+temp+"\\uidWeibosFc.txt", "\\s{1,}",0.8f, temp, base+temp+"\\dytText_train.txt", base+temp+"\\dytText_test.txt","\t\t");
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
		String base="F:\\ExpData\\DataIntegate\\source\\nne\\Weibos\\extractAllWeibo\\";

		String weiboId_vecFile=base+"weiboId_vector.txt";
		String reviewFormFile=base+"allReview.txt";
		
		String dirName="avg"+avgNum+"weibo";
		String destReview=base+dirName+"\\allReview.txt";
		String dest_avgWeibo_vec=base+dirName+"\\weiboId_vector.txt";
		Func.avgWeiboVec(reviewFormFile, weiboId_vecFile, avgNum, destReview, dest_avgWeibo_vec);

	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("hello");
		//excExtractWeibo();
		//excuidWeibos2uidWeiboId();
		//		Func.mergeUidAnfWords("F:\\ExpData\\DataIntegate\\source\\nne\\Weibos\\weiboIds.txt", "F:\\ExpData\\DataIntegate\\source\\nne\\Weibos\\oneLineWeibosFc.txt",
		//						"F:\\ExpData\\DataIntegate\\source\\nne\\Weibos\\weiboIdAndWeibosFc.txt", " ");

		//				Func.mergeUidAnfWords("F:\\ExpData\\DataIntegate\\source\\nne\\Weibos\\uid.txt", "F:\\ExpData\\DataIntegate\\source\\nne\\Weibos\\weibosFc.txt",
		//						"F:\\ExpData\\DataIntegate\\source\\nne\\Weibos\\uidWeibosFc.txt", " ");
		//excGenerateWeiboVec();
		//excGenerateUserVec();
		//excGenerateTrainAndTest();
		//excGenerateWeiboReview(20);
		//excGenerate();
		//FileTool.glanceFileContent("F:\\ExpData\\DataIntegate\\source\\nne\\Weibos\\weiboId_vector.txt", 1, 5);
		//excGenerateAvgXWeibosReview(5);
        //Func.extractNumWeibo("F:\\ExpData\\DataIntegate\\source\\nne\\Weibos\\extractAllWeibo\\avg5weibo\\allReview.txt", "F:\\ExpData\\DataIntegate\\source\\nne\\Weibos\\extractWeibo-20\\avg5weibo\\allReview.txt", 20);
		//Func.extractNumWeiboLineForm("F:\\ExpData\\DataIntegate\\source\\nne\\Weibos\\extractAllWeibo\\avg20weibo\\allReview.txt", "F:\\ExpData\\DataIntegate\\source\\nne\\Weibos\\extractWeibo-10\\avg20weiboLineForm\\allReview.txt", 10);
		//excGenerateUserVecLineForm();
	}

}
