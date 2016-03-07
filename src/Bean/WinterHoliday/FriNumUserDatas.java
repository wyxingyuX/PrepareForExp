package Bean.WinterHoliday;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FriNumUserDatas {
	//key is friNum, value is this userData list which friNum satisfy the key
	private Map<Integer,List<UserDataFull>> friNumUserDatasMap=new TreeMap<Integer,List<UserDataFull>>();


	public void add(UserDataFull userData){
		List<UserDataFull> udataList=this.friNumUserDatasMap.get(userData.friendCount);
		if(udataList==null){
			udataList=new LinkedList<UserDataFull>();	
		}
		udataList.add(userData);
		this.friNumUserDatasMap.put(userData.friendCount,udataList );
	}

	public Map<Integer,List<UserDataFull>> getFriNumUserDatasByLimitTagsNum(int startTagNum,int endTagNum) throws Exception{
		Map<Integer,List<UserDataFull>> friNumUserDatasByLimitTagsNum=new TreeMap<Integer,List<UserDataFull>>();
		for(Map.Entry<Integer, List<UserDataFull>> entry:this.friNumUserDatasMap.entrySet()){
			int friNum=entry.getKey();
			List<UserDataFull> udatasAll=entry.getValue();

			if(!friNumUserDatasByLimitTagsNum.containsKey(friNum)){
				friNumUserDatasByLimitTagsNum.put(friNum, new LinkedList<UserDataFull>());
			}
			List<UserDataFull> udatas=friNumUserDatasByLimitTagsNum.get(friNum);
			for(UserDataFull udata:udatasAll){
				if(this.isUdataTagsNumSatisfy(udata, startTagNum, endTagNum)){
					udatas.add(udata);
				}
			}
		}
		return friNumUserDatasByLimitTagsNum;
	}

	protected boolean isUdataTagsNumSatisfy(UserData udata,int startNum,int endNum) throws Exception{
		if((endNum>0&&startNum>endNum)||startNum<0){
			throw new Exception("startNum>endNum||startNum<0");
		}
		if(udata.tagCount>=startNum&&endNum<0){
			return true;
		}
		if(udata.tagCount>=startNum&&endNum>0&&udata.tagCount<endNum){
			return true;
		}
		return false;
	}

	public Map<Integer,List<UserDataFull>> getFriNumUserDatasMap() {
		return friNumUserDatasMap;
	}

	public void setFriNumUserDatasMap(Map<Integer,List<UserDataFull>> friNumUserDatasMap) {
		this.friNumUserDatasMap = friNumUserDatasMap;
	}


}
