package Bean.SKLE;

public class User {
	public String uid;
	public UserTextInfo userTextInfo;
	public UserRelationInfo userRelationInfo;

	public User()
	{
	
	}
	public User(String uid)
	{
		this.uid=uid;
		this.userTextInfo=new UserTextInfo();
		this.userRelationInfo=new UserRelationInfo();
	}
}
