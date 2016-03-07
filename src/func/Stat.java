package func;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import Bean.WinterHoliday.FriNumUserDatas;
import Bean.WinterHoliday.UserDataFull;
import utils.FileTool;

public class Stat {
	public static FriNumUserDatas generateFriNumUserDatas(String uidTagsFile,String uidTagsSeperater,
			String uidFrisFile,String uidFrisSeperater) throws Exception{
		BufferedReader uidTagsReader=FileTool.getBufferedReaderFromFile(uidTagsFile);
		BufferedReader uidFrisReader=FileTool.getBufferedReaderFromFile(uidFrisFile);

		String uidTagsLine="";
		String uidFrisLine="";
		FriNumUserDatas friNumUserDatas=new FriNumUserDatas();
		while((uidTagsLine=uidTagsReader.readLine())!=null&&(uidFrisLine=uidFrisReader.readLine())!=null){
			UserDataFull udata=new UserDataFull(uidTagsLine,uidTagsSeperater,uidFrisLine,uidFrisSeperater);
			friNumUserDatas.add(udata);
		}
		return friNumUserDatas;
	}

	public static Map<Integer,List<UserDataFull>> generateFriNumUserDatasByLimtTaggNum(FriNumUserDatas fnumUdatas,
			int startTagNum,int endTagNum) throws Exception{
		return fnumUdatas.getFriNumUserDatasByLimitTagsNum(startTagNum, endTagNum);
	}

	public static Map<Integer,List<UserDataFull>> generaterFriNumUserDatasByLimitFriNum(
			Map<Integer,List<UserDataFull>> fnumUdatas,int startFriNum,int endFriNum) throws Exception{
		Map<Integer,List<UserDataFull>> friNumUserDatasByLimitFriNum=new TreeMap<Integer,List<UserDataFull>>();
        
		//there just clone refrence not object value. this is a hidden dangerous
		for(Map.Entry<Integer,List<UserDataFull>> entry:fnumUdatas.entrySet()){
			int friNum=entry.getKey();
			if(endFriNum>0&&startFriNum>endFriNum){
				throw new Exception("startFriNum>endFriNum");
			}
			if(friNum>=startFriNum&&endFriNum<0){
				friNumUserDatasByLimitFriNum.put(entry.getKey(), entry.getValue());
			}
			if(friNum>=startFriNum&&endFriNum>0&&friNum<endFriNum){
				friNumUserDatasByLimitFriNum.put(entry.getKey(), entry.getValue());
			}
		}
		
		return friNumUserDatasByLimitFriNum;
	}
	

	public static void statUserTagsAndUserFris(Map<Integer,List<UserDataFull>> fnumUdatas,
			int startFriNum,int endFriNum,int step,String dest,boolean isAppend,String destSeperater) throws UnsupportedEncodingException, FileNotFoundException{
		PrintWriter writer=FileTool.getPrintWriterForFile(dest,isAppend);

		boolean isNewStep=true;
		int stepEnd=0;
		int count=0;
		for(int i=startFriNum;i<endFriNum;++i){
			if(isNewStep){
				//save last step stat info
				if(i!=startFriNum){
					writer.write(count+"\r\n");
				}

				//set new step 
				stepEnd=i+step-1;
				isNewStep=false;
				count=0;
				writer.write("["+i+", "+(stepEnd+1)+")"+destSeperater);


			}
			if(!isNewStep&&i==stepEnd){
				//prepare for next step
				isNewStep=true;
			}

			// count
			List<UserDataFull> udataList=fnumUdatas.get(i);
			if(udataList!=null){
				count+=udataList.size();
			}

			//last iter
			if(i==endFriNum-1){
				writer.write(count+"\r\n");
			}
		}

		writer.close();
	}

	public static void statUserTagsAndUserFris(Map<Integer,List<UserDataFull>> fnumUdatas,
			String dest,String destSeperater) throws UnsupportedEncodingException, FileNotFoundException{
		PrintWriter writer=FileTool.getPrintWriterForFile(dest);

		int max=0;
		for(Map.Entry<Integer, List<UserDataFull>> entry:fnumUdatas.entrySet()){
			if(max<entry.getKey()){
				max=entry.getKey();
			}
		}

		for(int i=0;i<=max;++i){
			List<UserDataFull> udatas=fnumUdatas.get(i);
			writer.write(i+destSeperater);
			if(udatas==null){
				writer.write(0+"\r\n");
			}else{
				writer.write(udatas.size()+"\r\n");
			}
		}
		writer.close();
	}

	public static int  statTotalUser(Map<Integer,List<UserDataFull>> fnumUdatas,String dest,boolean isAppend,String destSeperater) throws UnsupportedEncodingException, FileNotFoundException{
		PrintWriter writer=FileTool.getPrintWriterForFile(dest,isAppend);
		int totalUserNum=countMapValueItems(fnumUdatas);
		writer.write("toal user num:"+destSeperater+totalUserNum+"\r\n");
		writer.close();
		return totalUserNum;
	}
	public static int statOtherInfo(Map<Integer,List<UserDataFull>> fnumUdatas,String dest,boolean isAppend,String destSeperater) throws UnsupportedEncodingException, FileNotFoundException
	{
		PrintWriter writer=FileTool.getPrintWriterForFile(dest,isAppend);
		int totalFriNum=0;
		int minFriNum=-1;
		int maxFriNum=0;
		
		int totalTagNum=0;
		int minTagNum=-1;
		int maxTagNum=0;
		
		int totalUserNum=countMapValueItems(fnumUdatas);
		writer.write("toal user num:"+destSeperater+totalUserNum+"\r\n");
		
		for(Map.Entry<Integer,List<UserDataFull>> entry:fnumUdatas.entrySet())
		{
			int friNum=entry.getKey();
			totalFriNum+=friNum*entry.getValue().size();
			if(minFriNum<0)
			{
				minFriNum=friNum;
			}
			if(minFriNum>friNum)
			{
				minFriNum=friNum;
			}
			if(maxFriNum<friNum)
			{
				maxFriNum=friNum;
			}
			
			for(UserDataFull user:entry.getValue())
			{
				int tagNum=user.tagCount;
				totalTagNum+=tagNum;
				if(minTagNum<0)
				{
					minTagNum=tagNum;
				}
				if(minTagNum>tagNum)
				{
					minTagNum=tagNum;
				}
				if(maxTagNum<tagNum)
				{
					maxTagNum=tagNum;
				}
			}
		}
	
		writer.write("toal fri num:"+destSeperater+totalFriNum+destSeperater+
				", min fri num:"+destSeperater+minFriNum+destSeperater+
				",max fri num:"+destSeperater+maxFriNum+destSeperater+
				",avg fri num:"+destSeperater+(totalFriNum*1.0)/totalUserNum*1.0+"\r\n");
		writer.write("toal Tag num:"+destSeperater+totalTagNum+destSeperater+
				", min Tag num:"+destSeperater+minTagNum+destSeperater+
				",max Tag num:"+destSeperater+maxTagNum+destSeperater+
				",avg Tag num:"+destSeperater+(totalTagNum*1.0)/totalUserNum*1.0+"\r\n");
		writer.close();
		return totalUserNum;
		
	}
	
	public static int countMapValueItems(Map<Integer,List<UserDataFull>> fnumUdatas){
		int totalUserNum=0;
		for(Map.Entry<Integer, List<UserDataFull>> entry:fnumUdatas.entrySet()){
			totalUserNum+=entry.getValue().size();
		}
		return totalUserNum;
	}
	public static int countNoVecUserNum(String uidVecFile,String seperater) throws IOException{
		int count=0;
		BufferedReader reader=FileTool.getBufferedReaderFromFile(uidVecFile);
		String line="";
		reader.readLine();reader.readLine();
		
		while((line=reader.readLine())!=null){
			String[] elms=line.split(seperater,2);
			if(elms.length<2){
				++count;
			}
		}
		System.out.println(count+" user no vec");
		return count;
	}

}
