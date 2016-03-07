package Bean.SKLE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import func.ExtractData;
import utils.FileTool;
import utils.IO;

public class UserMemDB {
	public Map<String,User> uidUserMap;

	public UserMemDB() 
	{
		// TODO Auto-generated constructor stub
	}
	public UserMemDB(String uidTagsFile,String uidTagsSeperater,String uidFrisFile,String uidFrisSeperater) throws IOException
	{
		this.uidUserMap=loadUidUserMap(uidTagsFile, uidTagsSeperater, uidFrisFile, uidFrisSeperater);
	}



	public  Map<String,User> loadUidUserMap(String uidTagsFile,String uidTagsSeperater,String uidFrisFile,String uidFrisSeperater )
			throws IOException
	{
		Map<String,User> uidUserMap=loadUidRawUserMap( uidTagsFile, uidTagsSeperater);
		fillUserRelationInfo(uidFrisFile,uidFrisSeperater,uidUserMap);
		return uidUserMap;
	}
	private static Map<String,User> loadUidRawUserMap(String uidTagsFile,String uidTagsSeperater) throws IOException
	{
		Map<String,User> uidRawUserMap=new HashMap<String,User>();
		BufferedReader reader=FileTool.getBufferedReaderFromFile(uidTagsFile);
		String line="";
		while((line=reader.readLine())!=null)
		{
			String[] elms=line.split(uidTagsSeperater,2);
			String uid=elms[0].trim();
			User user=new User(uid);
			String tagsLine="";
			if(elms.length>1)
			{
				tagsLine=elms[1];
			}
			user.userTextInfo.addTags(tagsLine, uidTagsSeperater);
			uidRawUserMap.put(uid, user);
		}

		return uidRawUserMap;
	}

	private  void fillUserRelationInfo(String uidFrisFile,String uidFrisSeperater,Map<String,User> uidUserMap) throws IOException
	{
		BufferedReader reader=FileTool.getBufferedReaderFromFile(uidFrisFile);
		String line="";
		while((line=reader.readLine())!=null){
			String[] elms=line.split(uidFrisSeperater);
			String uid=elms[0].trim();
			User user=uidUserMap.get(uid);
			if(user==null)
			{
				user=new User(uid);
			}
			for(int i=1;i<elms.length;++i){
				String friUid=elms[i].trim();
				User fri=uidUserMap.get(friUid);
				if(fri==null)
				{
					fri=new User(friUid);
				}
				user.userRelationInfo.addFriend(fri);
			}
			uidUserMap.put(uid, user);//ingnore no problem
		}
	}


	public void printFriFri()
	{
		for(Map.Entry<String,User> entry:uidUserMap.entrySet())
		{
			User u=entry.getValue();
			if(u.userRelationInfo.getFrisSize()>0)
			{
				User fri1=u.userRelationInfo.friendList.get(0);
				System.out.println("uid:"+u.uid+"-->"+fri1.uid+"-->"+fri1.userRelationInfo.friendList.get(0).uid);
			}
		}
	}

}
