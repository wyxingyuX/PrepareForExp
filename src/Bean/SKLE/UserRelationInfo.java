package Bean.SKLE;

import java.util.LinkedList;
import java.util.List;

public class UserRelationInfo {
   public List<User> friendList;
   //public List<User> followerList;
   public UserRelationInfo()
   {
	   
   }
   public void addFriend(User fri)
   {
	   if(friendList==null){
		   friendList=new LinkedList<User>();
	   }
	   friendList.add(fri);
   }
   public List<User> getFriendList()
   {
	 return this.friendList;  
   }
   public int getFrisSize()
   {
	   if(this.friendList==null)
	   {
		   return -1;
	   }
	   return this.friendList.size();
   }
}
