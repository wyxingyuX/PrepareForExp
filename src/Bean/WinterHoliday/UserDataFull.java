package Bean.WinterHoliday;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class UserDataFull extends UserData{
	public List<String> tags=new LinkedList<String>();
	public List<String> friends=new LinkedList<String>();
	
	public UserDataFull(){
		
	}
	public UserDataFull(String uid,List<String> tags,List<String> friends){
		this.uid=uid;
		this.tags=tags;
		this.friends=friends;
		
		this.tagCount=tags.size();
		this.friendCount=friends.size();
	}
	public UserDataFull(String uidTagsLine,String uidTagsSeperater,
			String uidFrisLine,String uidFrisSeperater) throws Exception{
		String[] elmsUidTags=uidTagsLine.split(uidTagsSeperater);
		String[] elmsUidFris=uidFrisLine.split(uidFrisSeperater);
		if(!elmsUidTags[0].equals(elmsUidFris[0])){
			throw new Exception(uidTagsLine+"' uid="+elmsUidTags[0]+", but "+uidFrisLine+"'s uid="+elmsUidFris[0]+", not same!");
		}
		this.uid=elmsUidTags[0];
		for(int i=1;i<elmsUidTags.length;++i){
			this.tags.add(elmsUidTags[i]);
		}
		for(int i=1;i<elmsUidFris.length;++i){
			this.friends.add(elmsUidFris[i]);
		}
		
		this.tagCount=this.tags.size();
		this.friendCount=this.friends.size();
	}

}
